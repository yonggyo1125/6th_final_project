package org.sparta.monitoringserver.prompts.infrastructure;

import org.sparta.monitoringserver.prompts.domain.service.PromptValidator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PromptValidatorImpl implements PromptValidator {
    @Override
    public Mono<Boolean> isDuplicatedVersion(String promptName, String version) {
        return null;
    }
}
