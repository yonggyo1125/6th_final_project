package org.pgsg.file_service.infrastructure.storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file.storage.gcs")
public record GcsStorageProperties(
        String bucket,
        String projectId,
        String keyPath
) {}
