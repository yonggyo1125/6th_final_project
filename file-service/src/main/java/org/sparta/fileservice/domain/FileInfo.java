package org.sparta.fileservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.sparta.fileservice.domain.exception.FileStorageException;
import org.sparta.fileservice.domain.exception.ForbiddenException;
import org.sparta.fileservice.domain.exception.UnauthorizedException;
import org.sparta.fileservice.domain.service.FileUploader;
import org.sparta.fileservice.domain.service.RoleCheck;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@SQLRestriction("deleted_at IS NULL")
@Table(name="P_FILE_INFO", indexes = {
        @Index(name = "idx_file_group_query", columnList = "group_id, tag_name, deleted_at, created_at")
})
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
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(length = 65, updatable = false)
    private String createdBy;

    @Column(insertable = false)
    private LocalDateTime deletedAt;

    @Builder
    public FileInfo(String groupId, FileTag tagName, Storage storage, String fileName, String contentType, String filePath) {

        this.group = new FileGroup(groupId, tagName);
        this.metadata = new FileMeta(storage, fileName, contentType);
        this.filePath = filePath;
    }

    public static FileInfo upload(String groupId, FileTag tagName, Storage storage, FileSource source, FileUploader fileUploader, RoleCheck roleCheck) {

        // 파일 업로드는 로그인 상태에서만 가능
        if (!roleCheck.isLoggedIn()) {
            throw new UnauthorizedException("파일 업로드를 하려면 로그인이 필요합니다.");
        }

       // 파일 업로드 처리 먼저
        String filePath = fileUploader.upload(tagName, source);

        if (!StringUtils.hasText(filePath)) {
            throw new FileStorageException("파일 저장소 업로드에 실패하였습니다.");
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
            String contentType,
            long contentLength
    ) {}

    // 파일 삭제
    public void delete(RoleCheck roleCheck) {
        // 권한 체크
        checkDeleteAuthority(roleCheck);

        this.deletedAt = LocalDateTime.now();
    }

    // 관리자 또는 파일 소유자만 파일 삭제 가능
    private void checkDeleteAuthority(RoleCheck roleCheck) {
        if (!roleCheck.isMaster() && !roleCheck.isMine(this.createdBy)) {
            throw new ForbiddenException("파일 삭제권한이 없습니다.");
        }
    }
}
