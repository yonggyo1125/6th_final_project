package org.sparta.monitoringserver.prompts.application;

import lombok.RequiredArgsConstructor;
import org.sparta.monitoringserver.prompts.domain.Prompt;
import org.sparta.monitoringserver.prompts.domain.PromptRepository;
import org.sparta.monitoringserver.prompts.domain.exception.BadRequestException;
import org.sparta.monitoringserver.prompts.domain.exception.PromptNotFoundException;
import org.sparta.monitoringserver.prompts.domain.service.PromptValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PromptService {
    private final PromptRepository promptRepository;
    private final PromptValidator promptValidator;

    @Transactional
    public Mono<Long> registerPrompt(String name, String version, String content) {
        // 프롬프르 중복 검증
        return promptValidator.isDuplicatedVersion(name, version)
                .flatMap(isDuplicated -> {
                    if (isDuplicated) {
                        return Mono.error(new BadRequestException("이미 등록된 버전입니다. 프롬프트: %s, 버전: %s".formatted(name, version)));
                    }

                    // 프롬프트 저장 처리
                    Prompt prompt = Prompt.create(name, version, content);
                    return promptRepository.save(prompt)
                            .map(Prompt::getId);
                });
    }

    @Transactional
    public Mono<Void> activePrompt(Long id) {
        return promptRepository.findById(id)
                .switchIfEmpty(Mono.error(new PromptNotFoundException(id)))
                .map(prompt -> {
                    prompt.activate();
                    return prompt;
                })
                .flatMap(promptRepository::save)
                .then();
    }

    @Transactional(readOnly = true)
    public Mono<Prompt> getActiveLatestPrompt(String name) {
        return promptRepository.findActiveLatest(name)
                .switchIfEmpty(Mono.error(new PromptNotFoundException(name)));
    }

    @Transactional
    public Mono<Void> deletePrompt(Long id) {
        return promptRepository.findById(id)
                .switchIfEmpty(Mono.error(new PromptNotFoundException(id)))
                .map(prompt -> {
                    prompt.delete();
                    return prompt;
                })
                .flatMap(promptRepository::save)
                .then();
    }
}
