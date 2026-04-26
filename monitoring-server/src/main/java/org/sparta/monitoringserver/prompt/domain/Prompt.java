package org.sparta.monitoringserver.prompt.domain;

import lombok.*;
import org.sparta.monitoringserver.prompt.domain.exception.BadRequestException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
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

    @Column("prompt_name")
    private String promptName;

    @Column("version")
    private String version;

    @Column("model_name")
    private String modelName;

    @Column("description")
    private String description;

    @Column("max_tokens")
    private Integer maxTokens;

    @Column("temperature")
    private Double temperature;

    @Column("system_prompt")
    private String systemPrompt;

    @Column("content")
    private String content;

    @Column("is_active")
    private boolean isActive;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("modified_at")
    private LocalDateTime modifiedAt;

    @Column("deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    protected Prompt(Long id, String promptName, String version, String modelName, String description,
                     Integer maxTokens, Double temperature, String systemPrompt, String content,
                     boolean isActive, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;

        setTemplate(promptName, version, modelName, description, maxTokens, temperature, systemPrompt, content);
    }

    private void setTemplate(String promptName, String version, String modelName, String description,
                             Integer maxTokens, Double temperature, String systemPrompt, String content) {

        if (!StringUtils.hasText(version) || !version.matches("^\\d+\\.\\d+\\.\\d+$")) {
            throw new BadRequestException("버전 형식이 올바르지 않습니다. (예: 1.0.0)");
        }

        if (!StringUtils.hasText(systemPrompt)) {
            throw new BadRequestException("시스템 프롬프트는 필수 입력값 입니다.");
        }

        if (!StringUtils.hasText(content)) {
            throw new BadRequestException("프롬프트 본문은 필수 입력값 입니다.");
        }

        // LLM 파라미터 검증
        if (maxTokens != null && maxTokens < 1) {
            throw new BadRequestException("Max Tokens는 1 이상이어야 합니다.");
        }

        if (temperature != null && (temperature < 0.0 || temperature > 2.0)) {
            throw new BadRequestException("Temperature는 0.0에서 2.0 사이여야 합니다.");
        }

        this.promptName = promptName;
        this.version = version;
        this.modelName = StringUtils.hasText(modelName) ? modelName : "gpt-4o"; // 기본값 설정
        this.description = description;
        this.maxTokens = maxTokens != null ? maxTokens : 2000;
        this.temperature = temperature != null ? temperature : 0.7;
        this.systemPrompt = systemPrompt;
        this.content = content;
    }

    /**
     * 신규 프롬프트 생성을 위한 정적 팩토리 메서드
     */
    public static Prompt create(String promptName, String version, String modelName, String description,
                                Integer maxTokens, Double temperature, String systemPrompt, String content) {
        return Prompt.builder()
                .promptName(promptName)
                .version(version)
                .modelName(modelName)
                .description(description)
                .maxTokens(maxTokens)
                .temperature(temperature)
                .systemPrompt(systemPrompt)
                .content(content)
                .isActive(false)
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