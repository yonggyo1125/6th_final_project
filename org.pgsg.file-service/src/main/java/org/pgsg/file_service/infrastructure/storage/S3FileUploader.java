package org.pgsg.file_service.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.FileTag;
import org.pgsg.file_service.domain.service.FileUploader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "s3"
)
@RequiredArgsConstructor
public class S3FileUploader implements FileUploader {
    @Override
    public String upload(FileTag tag, FileInfo.FileSource source) {
        return "";
    }
}
