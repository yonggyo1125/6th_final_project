package org.sparta.fileservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    public String getPath(String uploadedDate) {
        return String.format("%s/%s", directory, uploadedDate);
    }
}
