package org.pgsg.file_service.domain.query;

import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.FileTag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileQueryRepository {
    Optional<FileInfo> findById(UUID fileId);
    List<FileInfo> findAll(String groupId, FileTag tag);

    default List<FileInfo> findAll(String groupId) {
        return findAll(groupId, null);
    }
}
