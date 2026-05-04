package org.sparta.fileservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.sparta.fileservice.domain.exception.FileUploadException;
import org.sparta.fileservice.domain.service.FileUploader;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@SQLRestriction("deleted_at IS NULL")
@Table(name="P_FILE_INFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FileGroup group;

    @Embedded
    private FileMeta metadata;

    private String filePath;

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;


    @Builder
    public FileInfo(String groupId, FileTag tagName, Storage storage, String fileName, String contentType, String filePath) {

        this.group = new FileGroup(groupId, tagName);
        this.metadata = new FileMeta(storage, fileName, contentType);
        this.filePath = filePath;
    }

    public static FileInfo upload(String groupId, FileTag tagName, Storage storage, FileSource source, FileUploader fileUploader) {

       // 파일 업로드 처리 먼저
        String filePath = fileUploader.upload(tagName, source);
        if (!StringUtils.hasText(filePath)) {
            throw new FileUploadException("파일 저장소 업로드에 실패하였습니다.");
        }

        return FileInfo.builder()
                .groupId(groupId)
                .tagName(tagName)
                .contentType(source.contentType())
                .fileName(source.originalFileName())
                .filePath(filePath)
                .storage(storage)
                .build();
    }

    @Builder
    public record FileSource(
            InputStream inputStream,
            String originalFileName,
            String contentType
    ) {}
}
