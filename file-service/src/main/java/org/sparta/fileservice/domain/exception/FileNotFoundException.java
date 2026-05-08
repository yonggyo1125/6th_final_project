package org.sparta.fileservice.domain.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(Long fileInfoId) {
        super("파일을 찾을 수 없습니다 - 파일 ID: " + fileInfoId);
    }
}
