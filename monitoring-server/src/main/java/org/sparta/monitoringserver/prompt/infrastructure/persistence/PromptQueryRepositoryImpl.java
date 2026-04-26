package org.sparta.monitoringserver.prompt.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.sparta.monitoringserver.prompt.domain.Prompt;
import org.sparta.monitoringserver.prompt.domain.query.PromptQueryRepository;
import org.sparta.monitoringserver.prompt.domain.query.PromptSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class PromptQueryRepositoryImpl implements PromptQueryRepository {

    private final DatabaseClient client;

    @Override
    public Flux<Prompt> findAll(PromptSearch search, Pageable pageable) {
        // 베이스 쿼리
        StringBuilder sql = new StringBuilder("SELECT * FROM prompts WHERE deleted_at IS NULL");

        // 동적 조건 추가
        applyConditions(sql, search);

        // 정렬 및 페이징
        sql.append(" ORDER BY created_at DESC LIMIT :limit OFFSET :offset");

        DatabaseClient.GenericExecuteSpec spec = client.sql(sql.toString())
                .bind("limit", pageable.getPageSize())
                .bind("offset", pageable.getOffset());

        // 명시적인 mapRow를 통해 새로 추가된 필드들(model_name, max_tokens 등)을 바인딩합니다.
        return bindParameters(spec, search)
                .map((row, metadata) -> Prompt.builder()
                        .id(row.get("id", Long.class))
                        .promptName(row.get("prompt_name", String.class))
                        .version(row.get("version", String.class))
                        .modelName(row.get("model_name", String.class))
                        .description(row.get("description", String.class))
                        .maxTokens(row.get("max_tokens", Integer.class))
                        .temperature(row.get("temperature", Double.class))
                        .systemPrompt(row.get("system_prompt", String.class))
                        .content(row.get("content", String.class))
                        .isActive(Boolean.TRUE.equals(row.get("is_active", Boolean.class)))
                        .createdAt(row.get("created_at", LocalDateTime.class))
                        .modifiedAt(row.get("modified_at", LocalDateTime.class))
                        .deletedAt(row.get("deleted_at", LocalDateTime.class))
                        .build()
                ).all();
    }

    @Override
    public Mono<Long> count(PromptSearch search) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM prompts WHERE deleted_at IS NULL");

        applyConditions(sql, search);

        DatabaseClient.GenericExecuteSpec spec = client.sql(sql.toString());
        return bindParameters(spec, search)
                .map((row, metadata) -> row.get(0, Long.class))
                .one()
                .defaultIfEmpty(0L);
    }

    private void applyConditions(StringBuilder sql, PromptSearch search) {
        if (search == null) return;

        if (StringUtils.hasText(search.promptName())) {
            sql.append(" AND prompt_name LIKE :promptName");
        }

        if (search.isActive() != null) {
            sql.append(" AND is_active = :isActive");
        }
    }

    private DatabaseClient.GenericExecuteSpec bindParameters(DatabaseClient.GenericExecuteSpec spec, PromptSearch search) {
        if (search == null) return spec;

        if (StringUtils.hasText(search.promptName())) {
            spec = spec.bind("promptName", "%" + search.promptName() + "%");
        }
        if (search.isActive() != null) {
            spec = spec.bind("isActive", search.isActive());
        }

        return spec;
    }
}