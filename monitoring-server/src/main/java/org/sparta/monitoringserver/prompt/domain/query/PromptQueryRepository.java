package org.sparta.monitoringserver.prompt.domain.query;

import org.sparta.monitoringserver.prompt.domain.Prompt;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PromptQueryRepository {
    Flux<Prompt> findAll(PromptSearch search, Pageable pageable);
    Mono<Long> count(PromptSearch search);

}
