package org.sparta.fileservice.domain.service;

import org.sparta.fileservice.domain.FileInfo;

public interface FileDownloader {
    FileDownloadContent download(FileInfo fileInfo);
}
