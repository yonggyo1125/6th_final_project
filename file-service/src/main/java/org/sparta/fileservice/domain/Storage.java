package org.sparta.fileservice.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Storage {
    LOCAL("서버 로컬 저장소"), // 로컬 서버
    S3("AWS S3 버킷"),
    GCS("GOOGLE Cloud Storage");

    private final String description;
}
