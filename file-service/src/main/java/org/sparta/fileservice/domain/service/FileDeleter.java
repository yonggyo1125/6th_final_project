package org.sparta.fileservice.domain.service;

import org.sparta.fileservice.domain.FileInfo;

public interface FileDeleter {
    void delete(FileInfo fileInfo);
}
