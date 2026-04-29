package com.goggles.user_service.user.application;

import com.goggles.user_service.user.application.dto.UserServiceDto;
import com.goggles.user_service.user.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원 가입 기능 테스트")
    void signup_test() {
        UserServiceDto.SignUp data = UserServiceDto.SignUp.builder()
                .email(UUID.randomUUID() + "@test.org")
                .password("_aA123456")
                .name("Test")
                .birthdate(LocalDate.of(1981, 1, 1))
                .gender(Gender.MALE)
                .phoneNumber("010-1000-1000")
                .emailConsent(true)
                .marketingConsent(true)
                .personalConsent(true)
                .nickname("Test")
                .interests(List.of(Interest.STUDY))
                .jobs(List.of(Job.SOFTWARE_ENGINEER))
                .educations(List.of("NONE"))
                .majors(List.of("NONE"))
                .build();

        UUID userId = userService.signUp(data);
        log.info("userId: {}", userId);

        User user = userRepository.findById(UserId.of(userId)).orElse(null);
        assertNotNull(user);
        assertEquals(userId, user.getId().getId());
        log.info("user: {}", user);


    }
}
