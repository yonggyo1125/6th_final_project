package org.pgsg.file_service.infrastructure.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.QFileInfo;
import org.pgsg.file_service.domain.query.FileQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileQueryRepositoryImpl implements FileQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<FileInfo> findById(UUID fileId) {

        QFileInfo fileInfo = QFileInfo.fileInfo;

        return Optional.ofNullable(
                queryFactory.selectFrom(fileInfo)
                        .where(
                                fileInfo.id.eq(fileId),
                                fileInfo.deletedAt.isNull()
                        )
                        .fetchOne()
        );
    }
}
