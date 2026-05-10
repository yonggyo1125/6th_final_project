//package org.sparta.fileservice.infrastructure.storage;
//
//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.Storage;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.sparta.fileservice.domain.FileInfo;
//import org.sparta.fileservice.domain.exception.FileStorageException;
//import org.sparta.fileservice.domain.service.FileDownloadContent;
//import org.sparta.fileservice.domain.service.FileDownloader;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Component;
//
//import java.nio.channels.Channels;
//
//@Slf4j
//@Component
//@ConditionalOnProperty(
//        name = "file.storage.type",
//        havingValue = "gcs"
//)
//@RequiredArgsConstructor
//public class GcsFileDownloader implements FileDownloader {
//
//    private final Storage storage;
//
//    @Value("${spring.cloud.gcp.storage.bucket}")
//    private String bucketName;
//
//
//    @Override
//    public FileDownloadContent download(FileInfo fileInfo) {
//        Blob blob = storage.get(bucketName, fileInfo.getFilePath());
//
//        if (blob == null || !blob.exists()) {
//            log.error("GCS 파일을 찾을 수 없습니다: {}",fileInfo.getFilePath());
//            throw new FileStorageException("파일을 찾을 수 없습니다.");
//        }
//
//        log.info("GCS 파일 다운로드 시작: {}", fileInfo.getFilePath());
//
//        return FileDownloadContent.builder()
//                .inputStream(Channels.newInputStream(blob.reader()))
//                .fileName(fileInfo.getMetadata().getFileName())
//                .contentType(blob.getContentType())
//                .contentLength(blob.getSize())
//                .build();
//    }
//}
