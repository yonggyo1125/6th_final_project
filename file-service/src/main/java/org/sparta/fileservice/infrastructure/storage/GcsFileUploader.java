//package org.sparta.fileservice.infrastructure.storage;
//
//import com.google.cloud.storage.BlobInfo;
//import com.google.cloud.storage.Storage;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.sparta.fileservice.domain.FileInfo;
//import org.sparta.fileservice.domain.FileTag;
//import org.sparta.fileservice.domain.exception.FileStorageException;
//import org.sparta.fileservice.domain.service.FileUploader;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@Slf4j
//@Component
//@ConditionalOnProperty(
//        name = "file.storage.type",
//        havingValue = "gcs"
//)
//@RequiredArgsConstructor
//public class GcsFileUploader implements FileUploader {
//
//    private final Storage storage;
//
//    @Value("${spring.cloud.gcp.storage.bucket}")
//    private String bucketName;
//
//    @Override
//    public String upload(FileTag tag, FileInfo.FileSource source) {
//        String storeFileName = StorageHelper.createStoreFileName(source.originalFileName());
//        String key = tag.name() + "/" + storeFileName;
//
//        try (InputStream inputStream = source.inputStream()) {
//            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, key)
//                    .setContentType(source.contentType())
//                    .build();
//
//            storage.create(blobInfo, inputStream.readAllBytes());
//
//            log.info("GCS 업로드 성공: {}/{}", bucketName, key);
//
//            return key;
//        } catch (IOException e) {
//            log.error("GCS 파일 스트림 읽기 실패: {}", e.getMessage(), e);
//            throw new FileStorageException("파일 처리 중 오류가 발생했습니다.");
//        } catch (Exception e) {
//            log.error("GCS SDK 오류: {}", e.getMessage(), e);
//            throw new FileStorageException("저장소 서버와의 통신에 실패했습니다.");
//        }
//    }
//}
