package org.pgsg.file_service.domain.exception;

import java.util.UUID;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(UUID fileId) {
        super("파일을 찾을 수 없습니다 - 파일 ID: " + fileId);
    }
}
