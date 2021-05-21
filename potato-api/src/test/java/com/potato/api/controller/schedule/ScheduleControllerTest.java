package com.potato.api.controller.schedule;

import com.potato.api.controller.ApiResponse;
import com.potato.api.controller.ControllerTestUtils;
import com.potato.domain.domain.board.admin.AdminBoard;
import com.potato.domain.domain.board.admin.AdminBoardRepository;
import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.api.service.board.organization.dto.response.AdminBoardInfoResponse;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import com.potato.api.service.schedule.dto.request.ScheduleRequest;
import com.potato.api.service.schedule.dto.response.ScheduleResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
class ScheduleControllerTest extends ControllerTestUtils {

    private ScheduleMockMvc scheduleMockMvc;

    @Autowired
    private AdminBoardRepository adminBoardRepository;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
        scheduleMockMvc = new ScheduleMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        adminBoardRepository.deleteAll();
        organizationBoardRepository.deleteAll();
    }

    @Test
    void 사월_한달간의_스케줄을_불러올때_관리자_게시글도_불러온다() throws Exception {
        // given
        String title = "관리자 게시물 제목";
        String content = "관리자 게시물 내용";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 1, 1, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 3, 1, 30);

        AdminBoard adminBoard = AdminBoard.builder()
            .administratorId(testMember.getId())
            .title(title)
            .content(content)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();
        adminBoardRepository.save(adminBoard);

        ScheduleRequest request = ScheduleRequest.testInstance(LocalDate.of(2021, 4, 1), LocalDate.of(2021, 4, 30));

        // when
        ApiResponse<ScheduleResponse> response = scheduleMockMvc.retrieveScheduleBetween(request);

        // hen
        assertThat(response.getData().getAdminBoards()).hasSize(1);
        assertAdminBoard(response.getData().getAdminBoards().get(0), adminBoard.getId(), title, content, startDateTime, endDateTime);

        assertThat(response.getData().getOrganizationBoards()).isEmpty();
    }

    @Test
    void 사월_한달간의_스케줄을_불러올때_그룹_게시글도_불러온다() throws Exception {
        // given
        String subDomain = "potato";
        String title = "그룹 게시물 제목";
        String content = "그룹 게시물 상세 내용";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 1, 1, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 3, 1, 30);
        String imageUrl = "https://groupimage.com";
        OrganizationBoardCategory category = OrganizationBoardCategory.RECRUIT;

        OrganizationBoard organizationBoard = OrganizationBoard.builder()
            .subDomain(subDomain)
            .title(title)
            .content(content)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .category(category)
            .memberId(testMember.getId())
            .build();

        organizationBoardRepository.save(organizationBoard);

        ScheduleRequest request = ScheduleRequest.testInstance(LocalDate.of(2021, 4, 1), LocalDate.of(2021, 4, 30));

        // when
        ApiResponse<ScheduleResponse> response = scheduleMockMvc.retrieveScheduleBetween(request);

        // then
        assertThat(response.getData().getAdminBoards()).isEmpty();
        assertThat(response.getData().getOrganizationBoards()).hasSize(1);
        assertOrganizationInfoResponse(response.getData().getOrganizationBoards().get(0), organizationBoard);
    }

    @Test
    void 게시글이_없을경우_빈배열을_반환한다() throws Exception {
        // given
        LocalDate startDate = LocalDate.of(2021, 4, 1);
        LocalDate endDate = LocalDate.of(2021, 4, 30);
        ScheduleRequest request = ScheduleRequest.testInstance(startDate, endDate);

        // when
        ApiResponse<ScheduleResponse> response = scheduleMockMvc.retrieveScheduleBetween(request);

        // then
        assertThat(response.getData().getOrganizationBoards()).isEmpty();
        assertThat(response.getData().getAdminBoards()).isEmpty();
    }

    private void assertAdminBoard(AdminBoardInfoResponse adminBoard, Long id, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        assertThat(adminBoard.getId()).isEqualTo(id);
        assertThat(adminBoard.getTitle()).isEqualTo(title);
        assertThat(adminBoard.getContent()).isEqualTo(content);
        assertThat(adminBoard.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(adminBoard.getEndDateTime()).isEqualTo(endDateTime);
    }

    private void assertOrganizationInfoResponse(OrganizationBoardInfoResponse response, OrganizationBoard organizationBoard) {
        assertThat(response.getId()).isEqualTo(organizationBoard.getId());
        assertThat(response.getSubDomain()).isEqualTo(organizationBoard.getSubDomain());
        assertThat(response.getTitle()).isEqualTo(organizationBoard.getTitle());
        assertThat(response.getContent()).isEqualTo(organizationBoard.getContent());
        assertThat(response.getStartDateTime()).isEqualTo(organizationBoard.getStartDateTime());
        assertThat(response.getEndDateTime()).isEqualTo(organizationBoard.getEndDateTime());
        assertThat(response.getLikesCount()).isEqualTo(organizationBoard.getLikesCount());
        assertThat(response.getType()).isEqualTo(organizationBoard.getCategory());
    }

}
