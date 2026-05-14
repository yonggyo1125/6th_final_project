package org.pgsg.file_service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.file_service.application.dto.FileServiceDto;
import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.FileRepository;
import org.pgsg.file_service.domain.FileTag;
import org.pgsg.file_service.domain.Storage;
import org.pgsg.file_service.domain.service.FileUploader;
import org.pgsg.file_service.domain.service.RoleChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileUploader fileUploader;
    private final RoleChecker roleChecker;

    @Value("${file.storage.type:local}")
    private String storage;

    @Transactional
    public UUID upload(FileServiceDto.FileUpload dto) {

        FileInfo fileInfo = FileInfo.upload(
            Storage.from(storage),
                dto.groupId(),
                FileTag.from(dto.tag()),
                dto.toSource(),
                fileUploader,
                roleChecker
        );

        fileRepository.save(fileInfo);

        return fileInfo.getId();
    }
}
