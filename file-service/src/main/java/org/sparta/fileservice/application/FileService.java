package org.sparta.fileservice.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.storage.type}")
    private String storage;

    @Transactional
    public Long upload(FileServiceDto.FileUpload dto) {

        return null;
    }
}
