package org.sparta.fileservice.infrastructure.storage;

import org.springframework.util.StringUtils;

import java.util.UUID;

public class StorageHelper {
    public static String createStoreFileName(String originalFileName) {
        String ext = extractExtension(originalFileName);
        return UUID.randomUUID() + (StringUtils.hasText(ext) ? "." + ext : "");
    }

    public static String extractExtension(String fileName) {
        if (!StringUtils.hasText(fileName)) return "";
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex <= 0) return "";
        return fileName.substring(lastIndex + 1);
    }
}
