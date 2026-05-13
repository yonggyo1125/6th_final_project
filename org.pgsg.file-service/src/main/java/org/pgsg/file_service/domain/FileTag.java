package org.pgsg.file_service.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.file_service.domain.exception.FileStorageException;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum FileTag {
    PROFILE("profile", "회원 프로필 이미지"),
    PRODUCT_LIST("product", "상품 목록 이미지"),
    PRODUCT_MAIN("product", "상품 메인 이미지"),
    PRODUCT_DESCRIPTION("product", "상품 상세설명 이미지"),
    LECTURE_LIST("lecture", "강의 목록 이미지"),
    LECTURE_MAIN("lecture", "강의 메인 이미지"),
    LECTURE_DESCRIPTION("lecture", "강의 상세설명 이미지"),
    LECTURE_VIDEO("lecture", "강의 동영상");

    private final String directory;
    private final String description;

    public static FileTag from(String tagName) {

        try {
            return FileTag.valueOf(tagName);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("지원하지 않는 파일 태그 - 태그: {}, 사유: {}", tagName, e.getMessage(), e);
            throw new FileStorageException("지원하지 않는 파일 태그입니다:" + tagName);
        }
    }
}
