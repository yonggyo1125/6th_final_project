package org.pgsg.file_service.domain.query;

import org.pgsg.file_service.domain.FileInfo;

import java.util.Optional;
import java.util.UUID;

public interface FileQueryRepository {
    Optional<FileInfo> findById(UUID fileId);
}
