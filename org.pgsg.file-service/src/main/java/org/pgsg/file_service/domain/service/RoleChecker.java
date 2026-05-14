package org.pgsg.file_service.domain.service;

import org.pgsg.file_service.domain.FileInfo;

import java.util.UUID;

public interface RoleChecker {
    boolean isMaster();
    boolean isMine(FileInfo fileInfo);
    boolean isLoggedIn();
    UUID getLoggedUserId();
}
