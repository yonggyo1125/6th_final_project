package org.pgsg.file_service.infrastructure.storage;

import org.springframework.util.StringUtils;

import java.util.UUID;

public class StorageHelper {

    public static String getStorageFileName(String fileName) {
        String ext =  fileName.lastIndexOf(".") != -1 ? fileName.substring(0, fileName.lastIndexOf(".") + 1) : "";

        return UUID.randomUUID().toString() + (StringUtils.hasText(ext) ? "." + ext : "");
    }
}
