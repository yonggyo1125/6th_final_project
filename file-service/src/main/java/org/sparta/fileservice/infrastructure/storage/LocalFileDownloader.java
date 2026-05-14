package org.sparta.fileservice.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.exception.FileStorageException;
import org.sparta.fileservice.domain.service.FileDownloadContent;
import org.sparta.fileservice.domain.service.FileDownloader;
import org.sparta.fileservice.infrastructure.storage.config.LocalStorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "local",
        matchIfMissing = true
)
@RequiredArgsConstructor
@EnableConfigurationProperties(LocalStorageProperties.class)
public class LocalFileDownloader implements FileDownloader {

    private final LocalStorageProperties properties;

    @Override
    public FileDownloadContent download(FileInfo fileInfo) {

        Path rootPath = Path.of(properties.path()).toAbsolutePath().normalize();
        Path filePath = rootPath.resolve(fileInfo.getFilePath()).normalize();

        if (!filePath.startsWith(rootPath)) {
            log.error("유효하지 않은 파일 경로 접근 시도: {}", filePath);
            throw new FileStorageException("유효하지 않은 파일 접근입니다.");
        }

        try {
            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                log.error("파일을 찾을 수 없거나 읽을 수 없음: {}", filePath);
                throw new FileStorageException("파일이 존재하지 않거나 읽을 수 없습니다.");
            }

            InputStream inputStream = Files.newInputStream(filePath);
            String contentType = Files.probeContentType(filePath);
            long contentLength = Files.size(filePath);

            log.info("로컬 파일 다운로드 데이터 준비 완료: {}", fileInfo.getMetadata().getFileName());

            return FileDownloadContent.builder()
                    .inputStream(inputStream)
                    .fileName(fileInfo.getMetadata().getFileName())
                    .contentType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .contentLength(contentLength)
                    .build();
        } catch (IOException e) {
            log.error("파일 읽기 중 오류 발생: {}", e.getMessage(), e);
            throw new FileStorageException("파일을 읽는 중 오류가 발생했습니다.");
        }
    }
}
