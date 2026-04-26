package org.sparta.monitoringserver.prompts.domain;

import lombok.*;
import org.sparta.monitoringserver.prompts.domain.exception.BadRequestException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@ToString
@Table("prompts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Prompt {

    @Id
    private Long id;

    private String promptName;

    private String version;

    private String systemPrompt;

    private String content;

    private boolean isActive;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    @Builder
    protected  Prompt(Long id, String promptName, String version, String systemPrompt, String content, boolean isActive) {
        this.id = id;
        this.isActive = isActive;
        setTemplate(promptName, version, systemPrompt, content);
    }

    private void setTemplate(String promptName, String version, String systemPrompt, String content) {

        if (!StringUtils.hasText(version)) {
            throw new BadRequestException("프롬프트 버전은 필수 입력값 입니다.");
        }

        if (!StringUtils.hasText(systemPrompt)) {
            throw new BadRequestException("시스템 프롬프트는 필수 입력값 입니다.");
        }

        if (!StringUtils.hasText(content)) {
            throw new BadRequestException("프롬프트 본문은 필수 입력값 입니다.");
        }

        this.promptName = promptName;
        this.version = version;
        this.systemPrompt = systemPrompt;
        this.content = content;
    }

    public static Prompt create(String promptName, String version, String systemPrompt, String content) {
        return Prompt.builder()
                .promptName(promptName)
                .version(version)
                .systemPrompt(systemPrompt)
                .content(content)
                .isActive(false) // 기본값 false
                .build();
    }


    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
