package org.pgsg.file_service.domain.service;

import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.FileTag;

/**
 *  파일 업로드 - local, S3, GCS
 *  업로드가 완료되면 업로드된 서버 경로 반환
 */
public interface FileUploader {
    String upload(FileTag tag, FileInfo.FileSource source);
}
