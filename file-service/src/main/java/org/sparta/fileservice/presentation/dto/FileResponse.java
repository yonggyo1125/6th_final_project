package org.sparta.fileservice.presentation.dto;

import lombok.*;
import org.sparta.fileservice.application.query.FileQueryResult;

import java.time.LocalDateTime;

public class FileResponse {

    public record Upload(
            Long fileId
    ) {}

    @Builder
    public record FileInfo(
        Long id,
        String groupId,
        String tagName,
        String fileName,
        String contentType,
        String extension,
        boolean isImage,
        boolean isVideo,
        String fileUrl,
        LocalDateTime createdAt
    ) {
        public static FileInfo from(FileQueryResult result) {
            return FileInfo.builder()
                    .id(result.id())
                    .groupId(result.groupId())
                    .tagName(result.tagName())
                    .fileName(result.fileName())
                    .contentType(result.contentType())
                    .extension(result.extension())
                    .isImage(result.isImage())
                    .isVideo(result.isVideo())
                    .fileUrl(result.fileUrl())
                    .createdAt(result.createdAt())
                    .build();
        }
    }

}
