package org.sparta.monitoringserver.prompts.domain.exception;

public class PromptNotFoundException extends NotFoundException {
    public PromptNotFoundException(String name) {
        super("프롬프트를 찾을 수 없습니다. 프롬프트명: %s".formatted(name));
    }

    public PromptNotFoundException(Long id) {
        super("프롬프트를 찾을 수 없습니다. 프롬프트 ID: %d".formatted(id));
    }
}
