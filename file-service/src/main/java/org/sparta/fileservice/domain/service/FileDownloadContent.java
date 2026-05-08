package org.sparta.fileservice.domain.service;

import lombok.Builder;

import java.io.InputStream;

@Builder
public record FileDownloadContent(
        InputStream inputStream,
        String fileName,
        String contentType,
        long contentLength
) {}
