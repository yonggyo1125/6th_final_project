package org.sparta.fileservice.application;

import lombok.*;

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
    }
}
