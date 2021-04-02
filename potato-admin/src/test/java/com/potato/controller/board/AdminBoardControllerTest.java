package com.potato.controller.board;

import com.potato.controller.ApiResponse;
import com.potato.controller.ControllerTestUtils;
import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.service.board.dto.request.CreateAdminBoardRequest;
import com.potato.service.board.dto.request.UpdateAdminBoardRequest;
import com.potato.service.board.dto.response.AdminBoardInfoResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
public class AdminBoardControllerTest extends ControllerTestUtils {

    private AdminBoardMockMvc adminBoardMockMvc;

    @Autowired
    private AdminBoardRepository adminBoardRepository;

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
        assertThat(response.getData()).isEqualTo("OK");
    }

    @Test
    void 관리자가_글을_수정하는_경우() throws Exception {
        //given
        String token = administratorMockMvc.getMockAdminToken();

        AdminBoard adminBoard = AdminBoard.builder()
            .title("학사")
            .administratorId(1L)
            .startDateTime(LocalDateTime.of(2021, 4, 1, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 4, 3, 0, 0))
            .build();
        adminBoardRepository.save(adminBoard);

        String title = "updateTitle";
        UpdateAdminBoardRequest request = UpdateAdminBoardRequest.testBuilder()
            .title(title)
            .adminBoardId(adminBoard.getId())
            .startDateTime(LocalDateTime.of(2021, 4, 1, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 4, 3, 0, 0))
            .build();

        //when
        ApiResponse<AdminBoardInfoResponse> response = adminBoardMockMvc.updateAdminBoard(request, token, 200);

        //then
        assertThat(response.getData().getTitle()).isEqualTo(title);
    }

}
