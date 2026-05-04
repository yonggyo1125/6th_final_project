package org.sparta.fileservice.domain.service;

import org.sparta.fileservice.domain.FileInfo;

public interface FileDownloader extends AutoCloseable {
    FileDownloadContent download(FileInfo fileInfo);
}
