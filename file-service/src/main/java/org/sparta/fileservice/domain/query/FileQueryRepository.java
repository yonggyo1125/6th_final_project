package org.sparta.fileservice.domain.query;

import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.FileTag;

import java.util.List;
import java.util.Optional;

public interface FileQueryRepository {
    Optional<FileInfo> findById(Long fileId);
    List<FileInfo> findAll(String groupId, FileTag tag);
    default List<FileInfo> findAll(String groupId) {
        return findAll(groupId, null);
    }
}
