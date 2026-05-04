package org.sparta.fileservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.sparta.fileservice.domain.exception.BadRequestException;
import org.springframework.util.StringUtils;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileGroup {
    @Column(length=45, nullable=false)
    private String groupId;

    @Column(name="tag_name", length=45)
    private String tag;

    protected FileGroup(String groupId, String tag) {

        if (!StringUtils.hasText(groupId)) {
            throw new BadRequestException("groupId는 필수입력 값 입니다.");
        }

        this.groupId = groupId;
        this.tag = tag;
    }
}
