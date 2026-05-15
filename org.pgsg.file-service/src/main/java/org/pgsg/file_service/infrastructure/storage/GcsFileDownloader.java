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
@EnableConfigurationProperties({GcsStorageProperties.class})
public class GcsFileDownloader implements FileDownloader {

    private final Storage storage;
    private final GcsStorageProperties properties;

    @Override
    public FileDownloadContent download(FileInfo fileInfo) {

        // GCS의 BlobId 생성 (버킷명과 경로)
        BlobId blobId = BlobId.of(properties.bucket(), fileInfo.getFilePath());

        // 파일 존재 여부 확인 및 객체 가져오기
        Blob blob = storage.get(blobId);
        if (blob == null || !blob.exists()) {
            log.error("GCS 파일 존재하지 않음 - Path: {}", fileInfo.getFilePath());
            throw new FileNotFoundException(fileInfo.getId());
        }

        // GCS ReadChannel을 InputStream으로 변환
        InputStream inputStream = Channels.newInputStream(blob.reader());

        log.info("GCS 파일 다운로드 스트림 생성 완료 - Path: {}", fileInfo.getFilePath());

        return FileDownloadContent.builder()
                .inputStream(inputStream)
                .fileName(fileInfo.getMetadata().getFileName())
                .contentLength(blob.getSize())
                .contentType(blob.getContentType())
                .build();
    }
}
