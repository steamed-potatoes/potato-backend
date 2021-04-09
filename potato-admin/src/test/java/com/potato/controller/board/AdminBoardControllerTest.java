package com.potato.controller.board;

import com.potato.controller.ApiResponse;
import com.potato.controller.ControllerTestUtils;
import com.potato.controller.advice.ErrorCode;
import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.service.board.dto.request.CreateAdminBoardRequest;
import com.potato.service.board.dto.request.UpdateAdminBoardRequest;
import com.potato.service.board.dto.response.AdminBoardInfoResponse;
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
        // given
        String token = administratorMockMvc.getMockAdminToken();

        String title = "title";
        String content = "content";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 3, 0, 0);

        CreateAdminBoardRequest request = CreateAdminBoardRequest.testBuilder()
            .content(content)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .title(title)
            .build();

        // when
        ApiResponse<AdminBoardInfoResponse> response = adminBoardMockMvc.createAdminBoard(request, token, 200);

        // then
        assertAdminBoardResponse(response.getData(), title, content, startDateTime, endDateTime);
    }

    @Test
    void 관리자만이_게시물을_생성할_수있다() throws Exception {
        // given
        CreateAdminBoardRequest request = CreateAdminBoardRequest.testBuilder()
            .title("title")
            .content("content")
            .startDateTime(LocalDateTime.of(2020, 3, 5, 0, 0))
            .endDateTime(LocalDateTime.of(2020, 3, 6, 0, 0))
            .build();

        // when
        ApiResponse<AdminBoardInfoResponse> response = adminBoardMockMvc.createAdminBoard(request, "empty", 401);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION.getCode());
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
        String content = "content";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 3, 0, 0);

        UpdateAdminBoardRequest request = UpdateAdminBoardRequest.testBuilder()
            .title(title)
            .content(content)
            .adminBoardId(adminBoard.getId())
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();

        // when
        ApiResponse<AdminBoardInfoResponse> response = adminBoardMockMvc.updateAdminBoard(request, token, 200);

        // then
        assertAdminBoardResponse(response.getData(), title, content, startDateTime, endDateTime);
    }

    @Test
    void 관리자만이_게시물을_수정할_수있다() throws Exception {
        // given
        UpdateAdminBoardRequest request = UpdateAdminBoardRequest.testBuilder()
            .title("title")
            .content("content")
            .startDateTime(LocalDateTime.of(2020, 3, 5, 0, 0))
            .endDateTime(LocalDateTime.of(2020, 3, 6, 0, 0))
            .build();

        // when
        ApiResponse<AdminBoardInfoResponse> response = adminBoardMockMvc.updateAdminBoard(request, "empty", 401);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION.getCode());
    }

    private void assertAdminBoardResponse(AdminBoardInfoResponse response, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(response.getEndDateTime()).isEqualTo(endDateTime);
    }

}
