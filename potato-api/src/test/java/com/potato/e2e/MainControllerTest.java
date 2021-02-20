package com.potato.e2e;

import com.potato.controller.MainController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class MainControllerTest extends AbstractControllerTest {

    @Autowired
    private MainController mainController;

    @Override
    protected Object controller() {
        return mainController;
    }

    @Test
    void healthCheck() throws Exception {
        this.mockMvc.perform(get("/ping"))
            .andExpect(status().isOk());
    }

}
