package org.sparta.monitoringserver.prompts.presentation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PromptRequest(
        @NotBlank(message = "프롬프트 명칭은 필수입니다.")
        String name,

        @NotBlank(message = "버전 정보는 필수입니다.")
        @Pattern(regexp = "^\\d+\\.\\d+\\.\\d+$", message = "버전 형식은 1.0.0과 같은 시맨틱 버전 형식을 따라야 합니다.")
        String version,

        String modelName,

        String description,

        @Min(value = 1, message = "Max Tokens는 최소 1 이상이어야 합니다.")
        Integer maxTokens,

        @Min(value = 0, message = "Temperature는 0.0 이상이어야 합니다.")
        @Max(value = 2, message = "Temperature는 2.0 이하이어야 합니다.")
        Double temperature,

        @NotBlank(message = "시스템 프롬프트는 필수입니다.")
        String systemPrompt,

        @NotBlank(message = "프롬프트 본문은 필수입니다.")
        String content
) {}