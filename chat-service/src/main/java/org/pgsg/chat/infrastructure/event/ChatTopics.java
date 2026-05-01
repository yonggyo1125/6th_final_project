package org.pgsg.chat.infrastructure.event;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="topics.chat")
public record ChatTopics(
        String roomCreated,
        String messageSent
) {}
