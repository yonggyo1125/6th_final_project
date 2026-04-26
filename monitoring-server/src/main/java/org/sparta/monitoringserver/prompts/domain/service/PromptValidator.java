package org.sparta.monitoringserver.prompts.domain.service;

import reactor.core.publisher.Mono;

public interface PromptValidator {
    Mono<Boolean> isDuplicatedVersion(String promptName, String version);
}
