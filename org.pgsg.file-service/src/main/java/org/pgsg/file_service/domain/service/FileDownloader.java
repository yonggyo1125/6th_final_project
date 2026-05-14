package org.pgsg.file_service.domain.service;

import org.pgsg.file_service.domain.FileInfo;

public interface FileDownloader {
    FileDownloadContent download(FileInfo fileInfo);
}
