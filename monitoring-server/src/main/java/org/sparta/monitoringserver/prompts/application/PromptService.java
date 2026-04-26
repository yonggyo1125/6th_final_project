package org.sparta.monitoringserver.prompts.application;

import lombok.RequiredArgsConstructor;
import org.sparta.monitoringserver.prompts.domain.Prompt;
import org.sparta.monitoringserver.prompts.domain.PromptRepository;
import org.sparta.monitoringserver.prompts.domain.exception.BadRequestException;
import org.sparta.monitoringserver.prompts.domain.exception.PromptNotFoundException;
import org.sparta.monitoringserver.prompts.domain.query.PromptQueryRepository;
import org.sparta.monitoringserver.prompts.domain.query.PromptSearch;
import org.sparta.monitoringserver.prompts.domain.service.PromptValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PromptService {
    private final PromptRepository promptRepository;
    private final PromptQueryRepository promptQueryRepository;
    private final PromptValidator promptValidator;

    @Transactional
    public Mono<Long> registerPrompt(String name, String version, String systemPrompt, String content) {
        // 프롬프르 중복 검증
        return promptValidator.isDuplicatedVersion(name, version)
                .flatMap(isDuplicated -> {
                    if (isDuplicated) {
                        return Mono.error(new BadRequestException("이미 등록된 버전입니다. 프롬프트: %s, 버전: %s".formatted(name, version)));
                    }

                    // 프롬프트 저장 처리
                    Prompt prompt = Prompt.create(name, version, systemPrompt, content);
                    return promptRepository.save(prompt)
                            .map(Prompt::getId);
                });
    }

    @Transactional
    public Mono<Void> activatePrompt(Long id) {
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

    @Transactional(readOnly = true)
    public Mono<Page<Prompt>> getAllPrompts(PromptSearch search, Pageable pageable) {
        return Mono.zip(
                promptQueryRepository.findAll(search, pageable).collectList(),
                promptQueryRepository.count(search)
        ).map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    @Transactional(readOnly = true)
    public Mono<Prompt> getLatestPromptForTemplate(String name) {
        if (!StringUtils.hasText(name)) {
            return Mono.empty();
        }
        return promptRepository.findActiveLatest(name);
    }
}
