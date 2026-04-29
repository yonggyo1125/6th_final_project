package com.goggles.user_service.user.application.dto;

import com.goggles.user_service.user.domain.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceDto {

    @Getter
    @Builder
    @ToString
    public static class SignUp {
        private UUID userId;
        private String email;
        private String password;
        private String name;
        private String nickname;
        private Gender gender;
        private LocalDate birthdate;

        private String phoneNumber;

        private List<Interest> interests;
        private List<Job> jobs;
        private List<String> educations;
        private List<String> majors;

        private boolean personalConsent;
        private boolean marketingConsent;
        private boolean emailConsent;

        public static SignUp from() {
            return null;
        }

        public static User toUser(UUID userId, UserServiceDto.SignUp data) {
            return User.builder()
                    .userId(userId)
                    .email(data.getEmail())
                    .gender(data.getGender())
                    .name(data.getName())
                    .nickname(data.getNickname())
                    .birthdate(data.getBirthdate())
                    .phoneNumber(data.getPhoneNumber())
                    .interests(data.getInterests())
                    .jobs(data.getJobs())
                    .educations(data.getEducations())
                    .majors(data.getMajors())
                    .personalConsent(data.isPersonalConsent())
                    .marketingConsent(data.isMarketingConsent())
                    .emailConsent(data.isEmailConsent())
                    .build();
        }
    }
}
