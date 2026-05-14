package org.pgsg.file_service.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.file_service.application.FileService;
import org.pgsg.file_service.presentation.dto.FileRequest;
import org.pgsg.file_service.presentation.dto.FileResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<FileResponse.Upload> upload(@Valid FileRequest.Upload request, @RequestPart("file") MultipartFile[] files) throws IOException {
        List<FileResponse.Upload> uploads = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            UUID fileId = fileService.upload(request.toServiceDto(file));
            uploads.add(new FileResponse.Upload(fileId));
        }

        return uploads;
    }

}
