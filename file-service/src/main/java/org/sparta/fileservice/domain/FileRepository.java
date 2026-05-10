package org.sparta.fileservice.domain;

import java.util.Optional;

public interface FileRepository {
    FileInfo save(FileInfo fileInfo);
    Optional<FileInfo> findById(Long fileInfoId);
}
