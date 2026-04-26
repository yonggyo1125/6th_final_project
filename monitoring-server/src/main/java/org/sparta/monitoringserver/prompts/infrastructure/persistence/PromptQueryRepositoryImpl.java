package org.sparta.monitoringserver.prompts.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.sparta.monitoringserver.prompts.domain.Prompt;
import org.sparta.monitoringserver.prompts.domain.query.PromptQueryRepository;
import org.sparta.monitoringserver.prompts.domain.query.PromptSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

        return bindParameters(spec, search).mapProperties(Prompt.class).all();
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
