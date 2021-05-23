package com.potato.admin.controller.board;

import com.potato.admin.controller.ApiResponse;
import com.potato.admin.controller.AbstractControllerTest;
import com.potato.admin.controller.advice.ErrorCode;
import com.potato.admin.controller.board.api.AdminBoardMockMvc;
import com.potato.domain.domain.board.admin.AdminBoard;
import com.potato.domain.domain.board.admin.AdminBoardRepository;
import com.potato.admin.service.board.dto.request.CreateAdminBoardRequest;
import com.potato.admin.service.board.dto.request.UpdateAdminBoardRequest;
import com.potato.admin.service.board.dto.response.AdminBoardInfoResponse;
import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.domain.board.organization.OrganizationBoardCreator;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class AdminBoardControllerTest extends AbstractControllerTest {

    private AdminBoardMockMvc adminBoardMockMvc;

    @Autowired
    private AdminBoardRepository adminBoardRepository;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @BeforeEach
    void setUp() {
        super.setup();
        adminBoardMockMvc = new AdminBoardMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        adminBoardRepository.deleteAll();
        organizationBoardRepository.deleteAll();
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
            .title(title)
            .content(content)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();

        // when
        ApiResponse<AdminBoardInfoResponse> response = adminBoardMockMvc.createAdminBoard(request, token, 200);

        // then
        assertAdminBoardResponse(response.getData(), title, content, startDateTime, endDateTime);
    }

    @Test
    void 관리자만이_게시물을_생성_할_수있다() throws Exception {
        // given
        CreateAdminBoardRequest request = CreateAdminBoardRequest.testBuilder()
            .title("title")
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
    void 관리자만이_게시물을_수정_할_수_있다() throws Exception {
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

    @Test
    void 관리자가_게시글을_삭제하는_경우() throws Exception {
        // given
        String token = administratorMockMvc.getMockAdminToken();

        String subDomain = "potato";
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 1L, "title1", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        // when
        ApiResponse<String> response = adminBoardMockMvc.deleteOrganizationBoard(organizationBoard.getId(), subDomain, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @Test
    void 관리자가_게시글을_삭제하려는데_해당_게시글이_없을_경우() throws Exception {
        // given
        String token = administratorMockMvc.getMockAdminToken();

        // when
        ApiResponse<String> response = adminBoardMockMvc.deleteOrganizationBoard(1L, "empty", token, 404);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION.getCode());
    }

}
