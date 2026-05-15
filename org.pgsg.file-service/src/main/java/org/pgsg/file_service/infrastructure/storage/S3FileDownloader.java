package org.pgsg.file_service.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.exception.FileNotFoundException;
import org.pgsg.file_service.domain.exception.FileStorageException;
import org.pgsg.file_service.domain.service.FileDownloadContent;
import org.pgsg.file_service.domain.service.FileDownloader;
import org.pgsg.file_service.infrastructure.storage.config.S3StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "s3"
)
@RequiredArgsConstructor
@EnableConfigurationProperties(S3StorageProperties.class)
public class S3FileDownloader implements FileDownloader {

    private final S3Client s3Client;
    private final S3StorageProperties properties;

    @Override
    public FileDownloadContent download(FileInfo fileInfo) {
        try {

            return null;
        } catch (NoSuchKeyException e) {
            log.error("AWS S3에 파일을 찾을 수 없음 - bucket: {}, key: {}, 사유: {}", properties.bucket(), fileInfo.getFilePath(), e.getMessage(), e);
            throw new FileNotFoundException(fileInfo.getId());
        } catch (Exception e) {
            log.error("AWS S3 다운로드 중 오류 발생 - 사유: {}", e.getMessage(), e);
            throw new FileStorageException("파일 다운로드 중 오류가 발생했습니다.");
        }
    }
}
