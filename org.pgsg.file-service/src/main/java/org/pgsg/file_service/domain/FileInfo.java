package org.pgsg.file_service.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;
import org.pgsg.common.domain.BaseEntity;

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
}
