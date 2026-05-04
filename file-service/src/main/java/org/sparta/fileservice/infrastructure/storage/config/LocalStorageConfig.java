package org.sparta.fileservice.infrastructure.storage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "local",
        matchIfMissing = true
)
@RequiredArgsConstructor
@EnableConfigurationProperties(LocalStorageProperties.class)
public class LocalStorageConfig implements WebMvcConfigurer {

    private final LocalStorageProperties properties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String handlerPattern = properties.getFormattedUrl() + "**";
        String rawPath = properties.path();
        String absolutePath = new File(rawPath).getAbsolutePath();

        if (!absolutePath.endsWith(java.io.File.separator)) {
            absolutePath += java.io.File.separator;
        }

        registry.addResourceHandler( handlerPattern)
                .addResourceLocations("file:" + absolutePath)
                .setCachePeriod(3600);
    }
}
