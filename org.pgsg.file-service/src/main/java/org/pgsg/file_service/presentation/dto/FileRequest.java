package org.pgsg.file_service.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileRequest {
    public record Upload(
            @NotBlank(message = "groupId는 필수 입력값입니다.")
            String groupId,

            @NotBlank(message = "tag는 필수 입력값입니다.")
            String tag
    ) {}
}
