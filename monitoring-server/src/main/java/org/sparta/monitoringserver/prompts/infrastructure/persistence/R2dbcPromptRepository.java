package org.sparta.monitoringserver.prompts.infrastructure.persistence;

import org.sparta.monitoringserver.prompts.domain.Prompt;
import org.sparta.monitoringserver.prompts.domain.PromptRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface R2dbcPromptRepository extends PromptRepository, ReactiveCrudRepository<Prompt, Long> {

    @Override
    Mono<Boolean> existsByPromptNameAndVersion(String promptName, String version);

    @Override
    @Query("""
        SELECT * FROM prompts
        WHERE prompt_name = :promptName
            AND is_active = true
            AND deleted_at IS NULL
        ORDER BY string_to_array(version, '.')::int[] DESC
        LIMIT 1        
    """)
    Mono<Prompt> findActiveLatest(@Param("promptName") String promptName);
}
