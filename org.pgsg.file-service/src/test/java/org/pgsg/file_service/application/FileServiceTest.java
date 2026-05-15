package org.pgsg.file_service.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pgsg.file_service.application.dto.FileServiceDto;
import org.pgsg.file_service.domain.service.RoleChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.mockito.BDDMockito.given;

@Slf4j
@SpringBootTest
public class FileServiceTest {
    @Autowired
    FileService fileService;

    MockMultipartFile file;

    @MockitoBean
    RoleChecker roleChecker;

    @BeforeEach
    void setup() {
        file = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, new byte[] {1,2,3,4,5,6,7,8});

        given(roleChecker.isLoggedIn()).willReturn(true);
    }


    @Test
    @DisplayName("파일 업로드 테스트")
    void fileUploadTest() throws Exception {
        FileServiceDto.FileUpload dto = FileServiceDto.FileUpload.builder()
                .groupId("test-group")
                .tag("PROFILE")
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .inputStream(file.getInputStream())
                .build();

        UUID fileId = fileService.upload(dto);
        log.info("fileId: {}", fileId);
    }
}
