package org.sparta.fileservice.domain;

import java.util.Optional;

public interface FileInfoRepository {
    FileInfo save(FileInfo fileInfo);
    Optional<FileInfo> findById(Long fileInfoId);
}
