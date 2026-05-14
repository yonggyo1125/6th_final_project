package org.pgsg.file_service.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.FileTag;
import org.pgsg.file_service.domain.exception.FileStorageException;
import org.pgsg.file_service.domain.service.FileUploader;
import org.pgsg.file_service.infrastructure.storage.config.LocalStorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "local",
        matchIfMissing = true
)
@RequiredArgsConstructor
@EnableConfigurationProperties(LocalStorageProperties.class)
public class LocalFileUploader implements FileUploader {

    private final LocalStorageProperties properties;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 태그디렉토리/년월일/UUID.확장자
     *
     */
    @Override
    public String upload(FileTag tag, FileInfo.FileSource source) {
        // 서버 업로드 경로
        String today = formatter.format(LocalDate.now());
        String relativePath = "%s/%s".formatted(tag.getDirectory(), today);
        Path parentPath = Path.of(properties.path()).toAbsolutePath().normalize();
        Path targetDirectory = parentPath.resolve(relativePath);

        // 업로드할 디렉토리가 없다면 생성
        try {
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }

            // 파일 업로드 파일명 생성
            String storageFileName = StorageHelper.getStorageFileName(source
                    .originalFileName());
            // 파일 업로드 경로 생성
            Path targetFile = targetDirectory.resolve(storageFileName);

            // 파일 업로드
            Files.copy(source.inputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            log.info("로컬 파일 업로드 성공 - 업로드 경로: {}", targetFile);

            return relativePath + "/" + storageFileName;

        } catch (IOException e) {
            log.error("로컬 파일 업로드 실패 - 업로드 경로: {}, 사유: {}", targetDirectory, e.getMessage(), e);
            throw new FileStorageException("파일 저장 중 시스템 오류가 발생했습니다.");
        }
    }
}
