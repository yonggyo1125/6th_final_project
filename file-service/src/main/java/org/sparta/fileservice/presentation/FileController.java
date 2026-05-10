package org.sparta.fileservice.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.fileservice.application.FileService;
import org.sparta.fileservice.application.dto.FileServiceDto;
import org.sparta.fileservice.presentation.dto.FileRequest;
import org.sparta.fileservice.presentation.dto.FileResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<FileResponse.Upload> upload(@Valid FileRequest.FileUpload request, @RequestPart("file") MultipartFile[] files) throws IOException {

        log.info("파일 업로드 요청 수신 - 그룹: {}, 개수: {}", request.getGroupId(), files.length);

        List<FileResponse.Upload> uploads = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            Long fileId = fileService.upload(request.toServiceDto(file));
            uploads.add(FileResponse.Upload.of(fileId));
        }

        return uploads;
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable("fileId") Long fileId) {

        log.info("파일 다운로드 요청 수신 - ID: {}", fileId);

        FileServiceDto.FileDownload downloadDto = fileService.download(fileId);

        String encodedFileName = URLEncoder.encode(downloadDto.getFileName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(downloadDto.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .contentLength(downloadDto.getContentLength())
                .body(new InputStreamResource(downloadDto.getInputStream()));
    }
}
