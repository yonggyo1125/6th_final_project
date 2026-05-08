package org.sparta.fileservice.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.service.FileDownloadContent;
import org.sparta.fileservice.domain.service.FileDownloader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "gcs"
)
@RequiredArgsConstructor
public class GcsFileDownloader implements FileDownloader {
    @Override
    public FileDownloadContent download(FileInfo fileInfo) {
        return null;
    }
}
