package com.example.demo.jws_session12_bai5;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwsSession12Bai5ApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void getMissingStudentReturnsJson404() throws Exception {
        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Student not found with id: 999"))
                .andExpect(jsonPath("$.status").value(404));
    }
}
