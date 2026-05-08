package org.sparta.fileservice.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.FileTag;
import org.sparta.fileservice.domain.exception.FileStorageException;
import org.sparta.fileservice.domain.service.FileUploader;
import org.sparta.fileservice.infrastructure.storage.config.S3StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "s3"
)
@RequiredArgsConstructor
@EnableConfigurationProperties({S3StorageProperties.class})
public class S3FileUploader implements FileUploader {

    private final S3Client s3Client;
    private final S3StorageProperties properties;

    @Override
    public String upload(FileTag tag, FileInfo.FileSource source) {
        String storeFileName = StorageHelper.createStoreFileName(source.originalFileName());
        String key = tag.name().toLowerCase() + "/" +  storeFileName;

        try (InputStream inputStream = source.inputStream()) {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(properties.bucket())
                    .key(key)
                    .contentType(source.contentType())
                    .build();

            // S3 업로드 처리
            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, source.contentLength()));

            log.info("S3 업로드 성공: {}/{}", properties.bucket(), key);
            return key;
        } catch (IOException e) {
            log.error("S3 파일 스트림 읽기 실패: {}", e.getMessage(), e);
            throw new FileStorageException("파일 처리 중 오류가 발생했습니다.");
        } catch (Exception e) {
            log.error("AWS S3 SDK 오류: {}", e.getMessage(), e);
            throw new FileStorageException("저장소 서버와의 통신에 실패했습니다.");
        }
    }

}
