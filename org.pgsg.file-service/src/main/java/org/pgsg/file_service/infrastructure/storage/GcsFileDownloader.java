package org.pgsg.file_service.infrastructure.storage;

import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.service.FileDownloadContent;
import org.pgsg.file_service.domain.service.FileDownloader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "gcs"
)
public class GcsFileDownloader implements FileDownloader {
    @Override
    public FileDownloadContent download(FileInfo fileInfo) {
        return null;
    }
}
