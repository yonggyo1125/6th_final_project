package org.sparta.fileservice.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.FileTag;
import org.sparta.fileservice.domain.exception.FileStorageException;
import org.sparta.fileservice.domain.service.FileUploader;
import org.sparta.fileservice.infrastructure.storage.config.LocalStorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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

    @Override
    public String upload(FileTag tag, FileInfo.FileSource source) {
        Path rootPath = Path.of(properties.path()).toAbsolutePath().normalize();
        Path targetDirectory = rootPath.resolve(tag.name().toLowerCase());

        try {
            // 업로드 파일을 저장할 디렉토리가 없다면 생성
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }

            // 업로드될 파일명 및 경로 생성
            String originalFileName = source.originalFileName();
            String storeFileName = StorageHelper.createStoreFileName(originalFileName);
            Path targetFile = targetDirectory.resolve(storeFileName);

            // 파일 저장
            Files.copy(source.inputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            log.info("로컬 파일 업로드 성공 - 업로드 경로: {}", targetFile);

            return tag.name().toLowerCase() + "/" + storeFileName;
        } catch (IOException e) {
            log.error("로컬 파일 업로드 실패: {}", e.getMessage(), e);
            throw new FileStorageException("파일 저장 중 시스템 오류가 발생하였습니다.");
        }
    }
}
