package org.sparta.fileservice.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.fileservice.application.dto.FileServiceDto;
import org.sparta.fileservice.domain.FileInfo;
import org.sparta.fileservice.domain.FileInfoRepository;
import org.sparta.fileservice.domain.FileTag;
import org.sparta.fileservice.domain.Storage;
import org.sparta.fileservice.domain.exception.FileNotFoundException;
import org.sparta.fileservice.domain.service.FileDownloader;
import org.sparta.fileservice.domain.service.FileUploader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileUploader fileUploader;
    private final FileDownloader fileDownloader;
    private final FileInfoRepository fileInfoRepository;

    @Value("${file.storage.type}")
    private String storageType;

    @Transactional
    public Long upload(FileServiceDto.FileUpload dto) {

        log.info("파일 업로드 시작 - 그룹: {}, 태그: {}, 파일명: {}", dto.getGroupId(), dto.getTagName(), dto.getOriginalFileName());

        FileTag tag = FileTag.from(dto.getTagName());
        Storage storage = Storage.valueOf(this.storageType.toUpperCase());

        FileInfo fileInfo = FileInfo.upload(
                dto.getGroupId(),
                tag,
                storage,
                dto.toSource(),
                fileUploader
        );

        fileInfoRepository.save(fileInfo);

        log.info("파일 업로드 완료 - 파일 ID: {}, 저장소: {}, 저장경로: {}", fileInfo.getId(), fileInfo.getMetadata().getStorage().getDescription(), fileInfo.getFilePath());

        return fileInfo.getId();
    }

    @Transactional(readOnly = true)
    public FileServiceDto.FileDownload download(Long fileInfoId) {
        log.info("파일 다운로드 시작 - 파일 ID: {}", fileInfoId);
        FileInfo fileInfo = fileInfoRepository.findById(fileInfoId).orElseThrow(() -> new FileNotFoundException(fileInfoId));

        FileServiceDto.FileDownload downloadDto = FileServiceDto.FileDownload.from(
                fileDownloader.download(fileInfo)
        );

        log.info("파일 다운로드 준비 완료 - 파일명: {}, 크기: {} bytes",
                downloadDto.getFileName(), downloadDto.getContentLength());

        return downloadDto;
    }
}
