package org.sparta.monitoringserver.prompts.presentation;

import jakarta.validation.constraints.NotBlank;

public record PromptRequest(
        @NotBlank(message = "프롬프트 명칭은 필수입니다.")
        String name,

        @NotBlank(message = "버전 정보는 필수입니다.")
        String version,

        @NotBlank(message = "시스템 프롬프트는 필수입니다.")
        String systemPrompt,

        @NotBlank(message = "프롬프트 본문은 필수입니다.")
        String content
) {}