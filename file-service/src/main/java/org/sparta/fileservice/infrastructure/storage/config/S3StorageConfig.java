package org.sparta.fileservice.infrastructure.storage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "s3"
)
@RequiredArgsConstructor
public class S3StorageConfig implements WebMvcConfigurer {
}
