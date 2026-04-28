package com.goggles.user_service.user.domain.model2;

import com.goggles.user_service.user.domain.exception.InvalidInstructorDescriptionException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InstructorDescription {

    @Column(nullable = false, length = 20)
    private String field;

    @Column(nullable = false, length = 500)
    private String bio;

    @Column(nullable = false, length = 1000)
    private String career;

    @Column(nullable = false, length = 500)
    private String portfolio;

    private InstructorDescription(String field, String bio, String career, String portfolio){
        validate(field, bio, career, portfolio);
        this.field = field;
        this.bio = bio;
        this.career = career;
        this.portfolio = portfolio;
    }

    public static InstructorDescription of(String field, String bio, String career, String portfolio){
        return new InstructorDescription(field, bio, career, portfolio);
    }

    private void validate(String field, String bio, String career, String portfolio){
        if (field == null || field.isBlank()) {
            throw new InvalidInstructorDescriptionException("field");
        }
        if (bio == null || bio.isBlank()) {
            throw new InvalidInstructorDescriptionException("bio");
        }
        if (career == null || career.isBlank()) {
            throw new InvalidInstructorDescriptionException("career");
        }
        if (portfolio == null || portfolio.isBlank()) {
            throw new InvalidInstructorDescriptionException("portfolio");
        }
    }
}