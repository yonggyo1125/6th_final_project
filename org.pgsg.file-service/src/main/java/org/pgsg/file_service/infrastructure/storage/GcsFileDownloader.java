package org.pgsg.file_service.infrastructure.storage;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.exception.FileNotFoundException;
import org.pgsg.file_service.domain.service.FileDownloadContent;
import org.pgsg.file_service.domain.service.FileDownloader;
import org.pgsg.file_service.infrastructure.storage.config.GcsStorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.channels.Channels;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "gcs"
)
@RequiredArgsConstructor
@EnableConfigurationProperties(GcsStorageProperties.class)
public class GcsFileDownloader implements FileDownloader {

    private final Storage storage;
    private final GcsStorageProperties properties;

    @Override
    public FileDownloadContent download(FileInfo fileInfo) {

        // BlobId
        BlobId blobId = BlobId.of(properties.bucket(), fileInfo.getFilePath());

        // 파일 가져오기
        Blob blob = storage.get(blobId);
        if (blob == null || !blob.exists()) { // 파일 존재 여부 체크
            throw new FileNotFoundException(fileInfo.getId());
        }

        InputStream inputStream = Channels.newInputStream(blob.reader());

        return FileDownloadContent.builder()
                .inputStream(inputStream)
                .fileName(fileInfo.getMetadata().getFileName())
                .contentType(blob.getContentType())
                .contentLength(blob.getSize())
                .build();

    }
}
