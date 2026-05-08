package org.sparta.fileservice.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.FileTag;
import org.sparta.fileservice.domain.Storage;
import org.sparta.fileservice.domain.service.FileUploader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "gcs"
)
@RequiredArgsConstructor
public class GcsFileUploader implements FileUploader {

    private final Storage storage;

    @Override
    public String upload(FileTag tag, FileInfo.FileSource source) {
        return "";
    }
}
