package org.sparta.monitoringserver.prompts.infrastructure.persistence;

import org.sparta.monitoringserver.prompts.domain.Prompt;
import org.sparta.monitoringserver.prompts.domain.query.PromptQueryRepository;
import org.sparta.monitoringserver.prompts.domain.query.PromptSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class PromptQueryRepositoryImpl implements PromptQueryRepository {
    @Override
    public Flux<Prompt> findAll(PromptSearch search, Pageable pageable) {
        return null;
    }

    @Override
    public Mono<Long> count(PromptSearch search) {
        return null;
    }
}
