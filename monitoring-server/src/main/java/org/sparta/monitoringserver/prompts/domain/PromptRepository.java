package org.sparta.monitoringserver.prompts.domain;

import reactor.core.publisher.Mono;

public interface PromptRepository {
    Mono<Prompt> findById(Long id);
    Mono<Boolean> existsByPromptNameAndVersion(String promptName, String version);
    Mono<Prompt> findActiveLatest(String promptName);
    <S extends Prompt> Mono<S> save(S prompt); // 저장을 위해 추가
}
