package org.sparta.fileservice.presentation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        file1 = new MockMultipartFile("file", "image1.png", MediaType.IMAGE_PNG_VALUE, new byte[] {0,1,2,3,4,5,6});
        file2 = new MockMultipartFile("file", "image2.png", MediaType.IMAGE_PNG_VALUE, new byte[] {0,1,2,3,4,5,6});
    }

    @Test
    @DisplayName("로컬 파일 업로드 테스트")
    void localFileUploadTest() throws Exception {
        mockMvc.perform(multipart("/")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("groupId", "testgroup")
                        .param("tagName", "LECTURE_MAIN")
                .file(file1)
                .file(file2)).andDo(print());
    }

    @Test
    @DisplayName("파일 하나 조회")
    void fileSearchTest() throws Exception {
        mockMvc.perform(get("/1/view"))
                .andDo(print());
    }

    @Test
    @DisplayName("파일 목록 조회")
    void fileListSearchTest() throws Exception {
        mockMvc.perform(get("/search?groupId=testgroup"))
                .andDo(print());
    }
}
