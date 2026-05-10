package org.sparta.fileservice.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sparta.fileservice.application.dto.FileServiceDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileRequest {

    @Data
    public static class FileUpload {
        @NotBlank(message = "그룹 ID는 필수 입력값입니다.")
        private String groupId;
        @NotBlank(message = "태그이름은 필수 입력값입니다.")
        private String tagName;

        public FileServiceDto.FileUpload toServiceDto(MultipartFile file) throws IOException {
            return FileServiceDto.FileUpload.builder()
                    .groupId(this.groupId)
                    .tagName(this.tagName)
                    .originalFileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .inputStream(file.getInputStream())
                    .build();
        }
    }

    @Data
    public static class FileSearch {
        @NotBlank(message = "그룹 ID는 필수 입력값입니다.")
        private String groupId;
        private String tagName;
    }
}
