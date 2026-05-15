package org.pgsg.file_service.infrastructure.storage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "gcs"
)
@RequiredArgsConstructor
@EnableConfigurationProperties(GcsStorageProperties.class)
public class GcsStorageConfig {

    private final GcsStorageProperties properties;
}
