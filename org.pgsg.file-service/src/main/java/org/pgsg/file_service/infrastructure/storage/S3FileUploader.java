package org.pgsg.file_service.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.FileTag;
import org.pgsg.file_service.domain.exception.FileStorageException;
import org.pgsg.file_service.domain.service.FileUploader;
import org.pgsg.file_service.infrastructure.storage.config.S3StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "s3"
)
@RequiredArgsConstructor
@EnableConfigurationProperties(S3StorageProperties.class)
public class S3FileUploader implements FileUploader {

    private final S3Client s3Client;
    private final S3StorageProperties properties;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public String upload(FileTag tag, FileInfo.FileSource source) {
        String today = formatter.format(LocalDate.now());
        String storageFileName = StorageHelper.getStorageFileName(source.originalFileName());
        String relativePath = "%s/%s/%s".formatted(tag.getDirectory(), today, storageFileName);

        try (InputStream inputStream = source.inputStream()) {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(properties.bucket())
                    .key(relativePath)
                    .contentType(source.contentType())
                    .build();

            // S3 저장소 업로드
            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, source.contentLength()));

            return relativePath;
        } catch (Exception e) {
            log.error("AWS S3 파일 업로드 실패 - 사유: {}", e.getMessage(), e);
            throw new FileStorageException("파일 업로드 중 오류가 발생했습니다.");
        }
    }
}
