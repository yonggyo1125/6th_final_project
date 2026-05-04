package org.sparta.fileservice.domain.service;

import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.FileTag;
import org.sparta.fileservice.domain.Storage;

/**
 * 반환값은 업로드된 물리적 경로
 *
 */
public interface FileUploader extends AutoCloseable {
    String upload(Storage storage, FileTag tag, FileInfo.FileSource source);
}
