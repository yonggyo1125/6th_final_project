package org.sparta.fileservice.application.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.fileservice.domain.FileTag;
import org.sparta.fileservice.domain.exception.FileNotFoundException;
import org.sparta.fileservice.domain.query.FileQueryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileQueryService {

    private final FileQueryRepository queryRepository;

    @Value("${file.storage.endpoint}")
    private String storageEndpoint;

    public FileQueryResult findById(Long fileId) {
        log.info("파일 정보 조회 - ID: {}", fileId);

        return queryRepository.findById(fileId)
                .map(fileInfo -> FileQueryResult.from(fileInfo, storageEndpoint))
                .orElseThrow(() -> {
                   log.warn("파일 정보를 찾을 수 없음 - ID: {}", fileId);
                    return new FileNotFoundException(fileId);
                });
    }

    public List<FileQueryResult> findAll(String groupId, String tagName) {
        log.info("파일 목록 조회 - GROUP ID: {}, TAGNAME: {}", groupId, tagName);

        FileTag tag = tagName != null ? FileTag.from(tagName): null;

        return queryRepository.findAll(groupId, tag)
                .stream()
                .map(f -> FileQueryResult.from(f, storageEndpoint))
                .toList();
    }
}
