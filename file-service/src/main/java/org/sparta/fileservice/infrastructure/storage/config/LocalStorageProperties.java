package org.sparta.fileservice.infrastructure.storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "file.local.base")
public record LocalStorageProperties(
        @DefaultValue("./storage/files")
        String path,
        @DefaultValue("/upload/")
        String url
) {

    public String getFormattedUrl() {
        return url.endsWith("/") ? url : url + "/";
    }
}
