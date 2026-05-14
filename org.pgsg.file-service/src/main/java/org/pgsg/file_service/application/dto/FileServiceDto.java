package org.pgsg.file_service.application.dto;

import lombok.*;
import org.pgsg.file_service.domain.FileInfo;

import java.io.InputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileServiceDto {

    @Builder
    public record FileUpload(
            String groupId,
            String tag,
            InputStream inputStream,
            String originalFileName,
            String contentType,
            long contentLength
    ) {
        public FileInfo.FileSource toSource() {
            return FileInfo.FileSource.builder()
                    .inputStream(this.inputStream)
                    .originalFileName(this.originalFileName)
                    .contentType(this.contentType)
                    .contentLength(this.contentLength)
                    .build();
        }
    }
}
