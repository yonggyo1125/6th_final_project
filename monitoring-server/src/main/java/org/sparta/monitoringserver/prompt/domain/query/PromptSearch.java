package org.sparta.monitoringserver.prompt.domain.query;

public record PromptSearch(
    String promptName,
    Boolean isActive
) {}
