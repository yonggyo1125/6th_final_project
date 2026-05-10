package org.sparta.fileservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.fileservice.domain.exception.FileStorageException;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum FileTag {
    PROFILE("profile", "회원 프로필"),
    BOARD("board", "게시판"),
    LECTURE_LIST("lecture", "강의 목록"),
    LECTURE_MAIN("lecture", "강의 메인"),
    EDITOR_IMAGE("editor", "에디터 첨부 이미지"),
    TEMP("temp", "임시 파일");

    private final String directory;
    private final String description;

    public static FileTag from(String tagName) {
        try {
            return FileTag.valueOf(tagName.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("지원하지 않는 파일 태그 - 태그: {}, 메세지: {}", tagName, e.getMessage(), e);
            throw new FileStorageException("지원하지 않는 파일 태그입니다: " + tagName);
        }
    }
}
