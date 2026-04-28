package com.goggles.user_service.user.domain.model2;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Experience {
    @Column(length = 20)
    private String job;

    @Column(length = 100)
    private String education;

    @Column(length = 50)
    private String major;

    private Experience(String job, String education, String major){
        this.job = job;
        this.education = education;
        this.major = major;
    }

    public static Experience of(String job, String education, String major){
        return new Experience(job, education, major);
    }
}
