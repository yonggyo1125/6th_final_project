package org.pgsg.file_service.infrastructure.storage.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Configuration
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "gcs"
)
@RequiredArgsConstructor
@EnableConfigurationProperties(GcsStorageProperties.class)
public class GcsStorageConfig {
    private final GcsStorageProperties properties;


    @Bean
    public Storage googleCloundStorage() throws IOException {
        if (StringUtils.hasText(properties.keyPath())) {
            try (InputStream inputStream = Files.newInputStream(Path.of(properties.keyPath()), StandardOpenOption.READ)) {
                return StorageOptions.newBuilder()
                        .setProjectId(properties.projectId())
                        .setCredentials(GoogleCredentials.fromStream(inputStream))
                        .build()
                        .getService();

            }
        }

        return StorageOptions.newBuilder()
                .setProjectId(properties.projectId())
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()
                .getService();
    }

}
