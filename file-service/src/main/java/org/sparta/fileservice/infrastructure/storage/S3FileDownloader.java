package org.sparta.fileservice.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.exception.FileStorageException;
import org.sparta.fileservice.domain.service.FileDownloadContent;
import org.sparta.fileservice.domain.service.FileDownloader;
import org.sparta.fileservice.infrastructure.storage.config.S3StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "s3"
)
@RequiredArgsConstructor
@EnableConfigurationProperties({S3StorageProperties.class})
public class S3FileDownloader implements FileDownloader {

    private final S3Client s3Client;
    private final S3StorageProperties properties;

    @Override
    public FileDownloadContent download(FileInfo fileInfo) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(properties.bucket())
                    .key(fileInfo.getFilePath())
                    .build();

            ResponseInputStream<GetObjectResponse> responseStream = s3Client.getObject(request);
            GetObjectResponse response = responseStream.response();

            log.info("S3 파일 다운로드 시작 - Bucket: {}, Key: {}", properties.bucket(), fileInfo.getFilePath());

            return FileDownloadContent.builder()
                    .inputStream(responseStream)
                    .fileName(fileInfo.getFilePath())
                    .contentType(response.contentType())
                    .contentLength(response.contentLength())
                    .build();

        } catch (NoSuchKeyException e) {
            log.error("S3에 파일이 존재하지 않습니다: {}/{}", fileInfo.getFilePath(), e.getMessage(), e);
            throw new FileStorageException("파일을 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("S3 다운로드 중 오류 발생: {}", e.getMessage(), e);
            throw new FileStorageException("파일 다운로드 중 오류가 발생했습니다.");
        }
    }

}
