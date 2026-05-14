package org.pgsg.file_service.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.pgsg.common.domain.BaseEntity;
import org.pgsg.file_service.domain.exception.FileStorageException;
import org.pgsg.file_service.domain.exception.ForbiddenException;
import org.pgsg.file_service.domain.service.FileUploader;
import org.pgsg.file_service.domain.service.RoleChecker;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.UUID;

@Getter
@Entity
@ToString
@Table(name="p_file_info")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileInfo extends BaseEntity {

    @Id
    @Column(name="file_id", length=45)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private FileGroup group;

    @Embedded
    private FileMeta metadata;

    private String filePath; // 서버 업로드 경로

    @Builder
    protected FileInfo(Storage storage, String groupId, FileTag tag, String fileName, String contentType, long contentLength, String filePath) {
        this.group = new FileGroup(groupId, tag);
        this.metadata = new FileMeta(storage, fileName, contentType, contentLength);
        this.filePath = filePath;
    }

    public static FileInfo upload(Storage storage, String groupId, FileTag tag, FileSource source, FileUploader uploader) {
        // 파일 업로드는 로그인 사용자만 가능

        // 파일 업로드 진행
        String filePath = uploader.upload(tag, source);

        if (!StringUtils.hasText(filePath)) {
            throw new FileStorageException("파일 저장소 업로드에 실패하였습니다.");
        }

        // 파일 정보 엔티티 완성
        return FileInfo.builder()
                .storage(storage)
                .groupId(groupId)
                .tag(tag)
                .fileName(source.originalFileName())
                .contentType(source.contentType())
                .contentLength(source.contentLength())
                .filePath(filePath)
                .build();
    }

    public record FileSource(
            InputStream inputStream,
            String originalFileName,
            String contentType,
            long contentLength
    ) {}

    // MASTER 또는 파일 소유자만 삭제 가능
    public void delete(RoleChecker checker) {
        if (!checker.isMaster() && !checker.isMine(this)) {
            throw new ForbiddenException("파일 삭제권한이 없습니다.");
        }

        this.delete(checker.getLoggedUserId());
    }
}
