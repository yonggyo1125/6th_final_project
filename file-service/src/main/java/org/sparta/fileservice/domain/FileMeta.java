package org.sparta.fileservice.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.sparta.fileservice.domain.exception.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileMeta {

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Storage storage;

    @Column(length=120, nullable = false)
    private String fileName;

    @Column(length = 65, nullable = false)
    private String contentType;

    @Column(length=35)
    private String extension; // 확장자

    protected FileMeta(Storage storage, String fileName, String contentType) {
        if (!StringUtils.hasText(fileName)) {
            throw new BadRequestException("파일명은 필수입력 값 입니다.");
        }

        this.storage = (storage == null) ? Storage.LOCAL : storage;
        this.fileName = fileName;

        this.contentType = StringUtils.hasText(contentType) ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        this.extension = StringUtils.hasText(fileName) ? extractExtension(fileName) : null;

    }

    private String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "";
        }

        String ext = fileName.substring(dotIndex + 1).toLowerCase();

        return ext.length() > 35 ? ext.substring(0, 35) : ext;
    }
}
