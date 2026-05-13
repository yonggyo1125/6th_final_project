package org.pgsg.file_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.pgsg.file_service.domain.exception.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileMeta {
    @Enumerated(EnumType.STRING)
    @Column(length=15, nullable=false)
    private Storage storage;

    @Column(length=120, nullable = false)
    private String fileName; // 업로드시 원본파일명

    @Column(length=35)
    private String extension; // 파일 확장자

    @Column(length=65, nullable = false)
    private String contentType; // 파일 형식

    private long contentLength; // 파일 길이


    protected FileMeta(Storage storage, String fileName, String contentType, long contentLength) {
        if (storage == null) {
            throw new BadRequestException("파일 저장소는 필수 입력값 입니다.");
        }

        if (!StringUtils.hasLength(fileName)) {
            throw new BadRequestException("파일명은 필수 입력값 입니다.");
        }

        this.storage = storage;
        this.contentType = StringUtils.hasText(contentType) ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        this.fileName = fileName;
        this.contentLength = contentLength;
        this.extension = extractExtension(fileName);
    }


    // 확장자가 없는 파일은?
    // abc.
    private String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }

        return fileName.substring(dotIndex+1).toLowerCase();
    }

    /**
     * Content-Type
     *      image/png, image/gif, image/jpeg ...
     */
    public boolean isImage() {
        return StringUtils.hasText(contentType) && contentType.startsWith("image/");
    }

    /**
     * Content-Type
     *  video/mp3, video/ogg, ...
     */
    public boolean isVideo() {
        return StringUtils.hasText(contentType) && contentType.startsWith("video/");
    }
}
