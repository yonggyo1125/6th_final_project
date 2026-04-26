package org.sparta.monitoringserver.prompts.domain.query;

import org.sparta.monitoringserver.prompts.domain.Prompt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PromptQueryRepository {
    Flux<Prompt> findAll(PromptSearch search, Pageable pageable);
    Mono<Long> count(PromptSearch search);

}
