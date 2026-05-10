package org.sparta.fileservice.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.fileservice.application.FileService;
import org.sparta.fileservice.application.dto.FileServiceDto;
import org.sparta.fileservice.application.query.FileQueryService;
import org.sparta.fileservice.presentation.dto.FileRequest;
import org.sparta.fileservice.presentation.dto.FileResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final FileQueryService fileQueryService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<FileResponse.Upload> upload(@Valid FileRequest.FileUpload request, @RequestPart("file") MultipartFile[] files) throws IOException {

        log.info("파일 업로드 요청 수신 - 그룹: {}, 개수: {}", request.getGroupId(), files.length);

        List<FileResponse.Upload> uploads = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            Long fileId = fileService.upload(request.toServiceDto(file));
            uploads.add(new FileResponse.Upload(fileId));
        }

        return uploads;
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable("fileId") Long fileId) {

        log.info("파일 다운로드 요청 수신 - ID: {}", fileId);

        FileServiceDto.FileDownload downloadDto = fileService.download(fileId);

       String contentDisposition = ContentDisposition.attachment()
               .filename(downloadDto.getFileName(), StandardCharsets.UTF_8)
               .build()
               .toString();


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(downloadDto.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentLength(downloadDto.getContentLength())
                .body(new InputStreamResource(downloadDto.getInputStream()));
    }

    @DeleteMapping("/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@PathVariable("fileId") Long fileId) {
        log.info("파일 삭제 요청 - ID: {}", fileId);
        fileService.delete(fileId);
    }

    // 파일 단건 조회
    @GetMapping("/{fileId}/view")
    public FileResponse.FileInfo getFile(@PathVariable("fileId") Long fileId) {
        log.info("파일 조회 요청 - ID: {}", fileId);

        return FileResponse.FileInfo.from(fileQueryService.findById(fileId));
    }

    // 파일 목록 조회
    @GetMapping("/")
    public List<FileResponse.FileInfo> getFiles(@Valid FileRequest.FileSearch search) {
        String groupId = search.getGroupId();
        String tagName = search.getTagName();
        log.info("파일 목록 조회 요청 - 그룹: {}, 태그: {}", groupId, tagName);

        return fileQueryService.findAll(groupId, tagName).stream()
                .map(FileResponse.FileInfo::from)
                .toList();
    }


}
