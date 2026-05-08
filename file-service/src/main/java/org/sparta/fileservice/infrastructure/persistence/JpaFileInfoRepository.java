package org.sparta.fileservice.infrastructure.persistence;

import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.FileInfoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFileInfoRepository extends FileInfoRepository, JpaRepository<FileInfo, Long> {
}
