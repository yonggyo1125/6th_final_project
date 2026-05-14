package org.pgsg.file_service.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.service.FileDownloadContent;

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

    @Builder
    public record FileDownload(
            InputStream inputStream,
            String fileName,
            String contentType,
            long contentLength
    ) {
        public static FileDownload from(FileDownloadContent content) {
            return FileDownload.builder()
                    .inputStream(content.inputStream())
                    .fileName(content.fileName())
                    .contentType(content.contentType())
                    .contentLength(content.contentLength())
                    .build();
        }
    }
}
