package org.pgsg.file_service.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.pgsg.common.domain.BaseEntity;

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

    public static FileInfo upload(Storage storage, String groupId, FileTag tag, FileSource source) {

    }

    public record FileSource(
            InputStream inputStream,
            String originalFileName,
            String contentType,
            long contentLength
    ) {}
}
