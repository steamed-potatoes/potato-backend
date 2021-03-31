package com.potato.controller.admin;

import com.potato.controller.ApiResponse;
import com.potato.controller.ControllerTestUtils;
import com.potato.controller.board.AdminBoardMockMvc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureMockMvc
@SpringBootTest
public class AdministratorControllerTest extends ControllerTestUtils {

    @BeforeEach
    void setUp() {
        super.setup();
        administratorMockMvc = new AdministratorMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @Test
    public void 가입되어_있는_관리자에게서_세션을_얻는다() throws Exception {
        //when
        ApiResponse<String> session = administratorMockMvc.getSession(200);

        //then
        Assertions.assertThat(session.getData()).isNotNull();
    }

}
