package org.sparta.fileservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@SQLRestriction("deleted_at IS NULL")
@Table(name="P_FILE_INFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FileGroup group;

    @Embedded
    private FileMeta metadata;

    private String filePath;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
