package org.sparta.monitoringserver.prompt.domain.service;

import reactor.core.publisher.Mono;

public interface PromptValidator {
    Mono<Boolean> isDuplicatedVersion(String promptName, String version);
}
