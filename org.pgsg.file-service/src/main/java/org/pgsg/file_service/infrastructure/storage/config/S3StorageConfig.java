package org.pgsg.file_service.infrastructure.storage.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "s3"
)
@RequiredArgsConstructor
@EnableConfigurationProperties(S3StorageProperties.class)
public class S3StorageConfig {

    private final S3StorageProperties properties;
}
