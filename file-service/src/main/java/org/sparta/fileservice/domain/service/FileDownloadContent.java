package org.sparta.fileservice.domain.service;

import java.io.InputStream;

public record FileDownloadContent(
        InputStream inputStream,
        String fileName,
        String contentType,
        long contentLength
) {}
