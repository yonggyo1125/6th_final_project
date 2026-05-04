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
        havingValue = "s3"
)
@RequiredArgsConstructor
public class S3FileDownloader implements FileDownloader {
    @Override
    public FileDownloadContent download(FileInfo fileInfo) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
