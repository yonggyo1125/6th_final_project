package org.sparta.monitoringserver.prompt.infrastructure.persistence;

import org.sparta.monitoringserver.prompt.domain.Prompt;
import org.sparta.monitoringserver.prompt.domain.PromptRepository;
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
    WHERE prompt_name = :name 
      AND is_active = true 
      AND deleted_at IS NULL 
    ORDER BY CAST(string_to_array(version, '.') AS INTEGER[]) DESC 
    LIMIT 1
    """)
    Mono<Prompt> findActiveLatest(@Param("promptName") String promptName);


}
