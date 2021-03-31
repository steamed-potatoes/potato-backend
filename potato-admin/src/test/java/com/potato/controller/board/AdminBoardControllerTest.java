package com.potato.controller.board;

import com.potato.controller.ApiResponse;
import com.potato.controller.ControllerTestUtils;
import com.potato.service.board.dto.request.CreateAdminBoardRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@AutoConfigureMockMvc
@SpringBootTest
public class AdminBoardControllerTest extends ControllerTestUtils {

    private AdminBoardMockMvc adminBoardMockMvc;

    @BeforeEach
    void setUp() {
        super.setup();
        adminBoardMockMvc = new AdminBoardMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @Test
    void 관리자가_게시글_생성하는_경우() throws Exception {
        //given
        String token = administratorMockMvc.getMockAdminToken();

        CreateAdminBoardRequest request = CreateAdminBoardRequest.testBuilder()
            .startDateTime(LocalDateTime.of(2021, 4, 1, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 4, 3, 0, 0))
            .title("title")
            .build();

        //when
        ApiResponse<String> response = adminBoardMockMvc.createAdminBoard(request, token, 200);

        //then
        System.out.println("response = " + response);
        Assertions.assertThat(response.getData()).isEqualTo("OK");
    }

}
