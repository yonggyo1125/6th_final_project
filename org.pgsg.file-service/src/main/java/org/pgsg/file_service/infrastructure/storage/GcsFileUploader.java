package org.pgsg.file_service.infrastructure.storage;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.FileTag;
import org.pgsg.file_service.domain.exception.FileStorageException;
import org.pgsg.file_service.domain.service.FileUploader;
import org.pgsg.file_service.infrastructure.storage.config.GcsStorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "gcs"
)
@RequiredArgsConstructor
@EnableConfigurationProperties(GcsStorageProperties.class)
public class GcsFileUploader implements FileUploader {

    private final Storage storage;
    private final GcsStorageProperties properties;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public String upload(FileTag tag, FileInfo.FileSource source) {
        String today = formatter.format(LocalDate.now());
        String storageFileName = StorageHelper.getStorageFileName(source.originalFileName());
        String relativePath = "%s/%s/%s".formatted(tag.getDirectory(), today, storageFileName);

        try {
            BlobInfo blobInfo = BlobInfo.newBuilder(properties.bucket(), relativePath)
                    .setContentType(source.contentType())
                    .build();

            storage.createFrom(blobInfo, source.inputStream());

            return relativePath;
        } catch (Exception e) {
            log.error("Google Cloud Storage 파일 업로드 실패 - 사유: {}", e.getMessage(), e);
            throw new FileStorageException("파일 업로드 중 오류가 발생했습니다.");
        }

    }
}
