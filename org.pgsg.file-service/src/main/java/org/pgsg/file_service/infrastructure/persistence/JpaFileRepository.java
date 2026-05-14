package org.pgsg.file_service.infrastructure.persistence;

import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.FileRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaFileRepository extends FileRepository, JpaRepository<FileInfo, UUID> {
}
