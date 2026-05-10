package org.sparta.fileservice.infrastructure.persistence.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.FileTag;
import org.sparta.fileservice.domain.query.FileQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.sparta.fileservice.domain.QFileInfo.fileInfo;

@Repository
@RequiredArgsConstructor
public class FileQueryRepositoryImpl implements FileQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<FileInfo> findById(Long fileId) {

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(fileInfo)
                        .where(
                                fileInfo.id.eq(fileId),
                                fileInfo.deletedAt.isNull()
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<FileInfo> findAll(String groupId, FileTag tag) {
        return queryFactory
                .selectFrom(fileInfo)
                .where(
                        fileInfo.group.groupId.eq(groupId),
                        eqTag(tag),
                        fileInfo.deletedAt.isNull()
                )
                .orderBy(fileInfo.createdAt.asc(), fileInfo.id.asc())
                .fetch();

    }

    private BooleanExpression eqTag(FileTag tag) {
        return tag != null ? fileInfo.group.tag.eq(tag) : null;
    }
}
