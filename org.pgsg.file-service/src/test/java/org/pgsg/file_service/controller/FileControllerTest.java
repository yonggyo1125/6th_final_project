package org.pgsg.file_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    MockMvc mockMvc;

    MockMultipartFile file1;
    MockMultipartFile file2;

    @BeforeEach
    void setup() {
        file1 = new MockMultipartFile("file", "test1.png", MediaType.IMAGE_PNG_VALUE, new byte[] {1,2,3,4});
        file2 = new MockMultipartFile("file", "test2.png", MediaType.IMAGE_PNG_VALUE, new byte[] {1,2,3,4});
    }

    @Test
    @WithMockUser
    @DisplayName("파일 업로드 테스트")
    void fileUploadTest() throws Exception {
        mockMvc.perform(multipart("/")
                        .file(file1)
                        .file(file2)
                        .param("groupId", "test-group")
                        .param("tag", "PROFILE")
                .header("X-User-Id", UUID.randomUUID().toString())
                .header("X-User-Username", "test-user")
                .header("X-User-Roles", "MASTER")
                .header("X-User-Nickname", "test-nickname")
                .header("X-user-Enabled", "true")

        ).andDo(print());
    }
}
