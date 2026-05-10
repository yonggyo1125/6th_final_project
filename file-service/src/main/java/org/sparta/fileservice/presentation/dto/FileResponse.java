package org.sparta.fileservice.presentation.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileResponse {

    @Data
    @ToString
    @AllArgsConstructor
    public static class Upload {
        private Long fileId;

        public static Upload of(Long fileId) {
            return new Upload(fileId);
        }
    }

}
