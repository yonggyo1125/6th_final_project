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
    public Mono<Long> registerPrompt(String name, String version, String modelName, String description,
                                     Integer maxTokens, Double temperature, String systemPrompt, String content) {

        // 1. 프롬프트 중복 검증 (동일 이름 + 동일 버전 방지)
        return promptValidator.isDuplicatedVersion(name, version)
                .flatMap(isDuplicated -> {
                    if (isDuplicated) {
                        return Mono.error(new BadRequestException("이미 등록된 버전입니다. 프롬프트: %s, 버전: %s".formatted(name, version)));
                    }

                    // 2. 도메인 객체 생성 (새로운 필드들 포함)
                    Prompt prompt = Prompt.create(
                            name, version, modelName, description,
                            maxTokens, temperature, systemPrompt, content
                    );

                    // 3. 저장 및 ID 반환
                    return promptRepository.save(prompt)
                            .map(Prompt::getId);
                });
    }


    @Transactional
    public Mono<Void> activatePrompt(Long id) {
        return promptRepository.findById(id)
                .switchIfEmpty(Mono.error(new PromptNotFoundException(id)))
                .flatMap(targetPrompt ->
                        // 동일한 이름을 가진 다른 활성화된 프롬프트들을 모두 비활성화 (Batch 처리)
                        promptRepository.findAllByPromptNameAndIsActiveTrueAndIdNot(
                                        targetPrompt.getPromptName(),
                                        targetPrompt.getId()
                                )
                                .map(otherPrompt -> {
                                    otherPrompt.deactivate();
                                    return otherPrompt;
                                })
                                .collectList()
                                .flatMapMany(promptRepository::saveAll)
                                .then(Mono.just(targetPrompt))
                )
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
                promptQueryRepository.count(search).defaultIfEmpty(0L)
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