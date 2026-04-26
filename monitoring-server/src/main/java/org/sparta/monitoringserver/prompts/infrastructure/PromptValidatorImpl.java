package org.sparta.monitoringserver.prompts.infrastructure;

import lombok.RequiredArgsConstructor;
import org.sparta.monitoringserver.prompts.domain.service.PromptValidator;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PromptValidatorImpl implements PromptValidator {
    private final DatabaseClient databaseClient;

    @Override
    public Mono<Boolean> isDuplicatedVersion(String promptName, String version) {
        return databaseClient.sql("SELECT COUNT(*) FROM prompts WHERE prompt_name = :name AND version = :version AND deleted_at IS NULL")
                .bind("name", promptName)
                .bind("version", version)
                .map((row, metadata) -> row.get(0, Long.class))
                .one()
                .map(count -> count > 0)
                .defaultIfEmpty(false);
    }
}
