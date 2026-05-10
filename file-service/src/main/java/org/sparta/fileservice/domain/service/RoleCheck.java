package org.sparta.fileservice.domain.service;

public interface RoleCheck {
    boolean isMaster();
    boolean isMine(String fileOwner);
    boolean isLoggedIn();
}
