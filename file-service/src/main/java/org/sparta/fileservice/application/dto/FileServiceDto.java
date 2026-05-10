package org.sparta.fileservice.application.dto;

import lombok.*;
import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.service.FileDownloadContent;

import java.io.InputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileServiceDto {
    @Getter
    @Builder
    @ToString
    public static class FileUpload {
        private String groupId;
        private String tagName;
        private InputStream inputStream;
        private String originalFileName;
        private String contentType;
        private long contentLength;

        public FileInfo.FileSource toSource() {
            return FileInfo.FileSource.builder()
                    .contentType(this.contentType)
                    .contentLength(this.contentLength)
                    .inputStream(this.inputStream)
                    .originalFileName(this.originalFileName)
                    .build();
        }
    }

    @Getter
    @Builder
    @ToString
    public static class FileDownload {
        private InputStream inputStream;
        private String fileName;
        private String contentType;
        private long contentLength;

        public static FileDownload from(FileDownloadContent content) {
            return FileDownload.builder()
                    .inputStream(content.inputStream())
                    .contentType(content.contentType())
                    .contentLength(content.contentLength())
                    .fileName(content.fileName())
                    .build();
        }
    }
}
