package org.sparta.fileservice.application.query;

import lombok.Builder;
import org.sparta.fileservice.domain.FileInfo;

import java.time.LocalDateTime;

@Builder
public record FileQueryResult(
    Long id,
    String groupId,
    String tagName,
    String fileName,
    String contentType,
    String extension,
    boolean isImage,
    boolean isVideo,
    String filePath,
    String fileUrl,
    LocalDateTime createdAt
) {
    public static FileQueryResult from(FileInfo fileInfo) {
        return FileQueryResult.builder()
                .id(fileInfo.getId())
                .groupId(fileInfo.getGroup().getGroupId())
                .tagName(fileInfo.getGroup().getTag().name())
                .fileName(fileInfo.getMetadata().getFileName())
                .contentType(fileInfo.getMetadata().getContentType())
                .extension(fileInfo.getMetadata().getExtension())
                .isImage(fileInfo.getMetadata().isImage())
                .isVideo(fileInfo.getMetadata().isVideo())
                .filePath(fileInfo.getFilePath())
                .createdAt(fileInfo.getCreatedAt())
                .build();
    }
}
