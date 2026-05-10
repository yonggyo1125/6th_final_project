package org.sparta.fileservice.infrastructure.persistence;

import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.FileRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFileRepository extends FileRepository, JpaRepository<FileInfo, Long> {
}
