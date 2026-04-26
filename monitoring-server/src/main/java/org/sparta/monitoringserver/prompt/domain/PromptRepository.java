package org.sparta.monitoringserver.prompt.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PromptRepository {
    Mono<Prompt> findById(Long id);
    Mono<Boolean> existsByPromptNameAndVersion(String promptName, String version);
    Mono<Prompt> findActiveLatest(String promptName);
    <S extends Prompt> Mono<S> save(S prompt); // 저장을 위해 추가
    <S extends Prompt> Flux<S> saveAll(Iterable<S> entities);

    Flux<Prompt> findAllByPromptNameAndIsActiveTrueAndIdNot(String promptName, Long id);
}
