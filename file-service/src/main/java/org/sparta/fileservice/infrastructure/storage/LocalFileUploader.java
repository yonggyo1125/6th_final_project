package org.sparta.fileservice.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.FileTag;
import org.sparta.fileservice.domain.service.FileUploader;
import org.sparta.fileservice.infrastructure.storage.config.LocalStorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "local",
        matchIfMissing = true
)
@RequiredArgsConstructor
@EnableConfigurationProperties(LocalStorageProperties.class)
public class LocalFileUploader implements FileUploader {

    private final LocalStorageProperties properties;

    @Override
    public String upload(FileTag tag, FileInfo.FileSource source) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
