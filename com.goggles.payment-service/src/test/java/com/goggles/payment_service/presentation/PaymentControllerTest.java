package com.goggles.payment_service.presentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles({"kafka", "topics", "dev"})
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void loggedUserTest() throws Exception {
        mockMvc.perform(get("/test")
                //.header("X-User-Id", "d63613a0-3aa1")
                //.header("X-User-Name", "Test")
                //.header("X-User-Email", "test@test.org")
                //.header("X-User-Role", "MASTER")
        ).andDo(print());
    }
}
