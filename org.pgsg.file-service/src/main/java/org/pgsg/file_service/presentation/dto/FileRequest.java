package org.pgsg.file_service.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.pgsg.file_service.application.dto.FileServiceDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileRequest {
    public record Upload(
            @NotBlank(message = "groupId는 필수 입력값입니다.")
            String groupId,

            @NotBlank(message = "tag는 필수 입력값입니다.")
            String tag
    ) {
        public FileServiceDto.FileUpload toServiceDto(MultipartFile file) throws IOException {
            return FileServiceDto.FileUpload.builder()
                    .groupId(this.groupId)
                    .tag(this.tag)
                    .inputStream(file.getInputStream())
                    .originalFileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();
        }
    }

    public record FileSearch(
            @NotBlank(message = "groupId는 필수 입력값입니다.")
            String groupId,
            String tag
    ) {}
}
