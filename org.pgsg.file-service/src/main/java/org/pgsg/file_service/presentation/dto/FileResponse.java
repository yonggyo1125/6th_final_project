package org.pgsg.file_service.presentation.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileResponse {
    public record Upload(
            UUID fileId
    ) {}
}
