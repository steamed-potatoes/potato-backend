package com.potato.controller.schedule;

import com.potato.controller.ApiResponse;
import com.potato.controller.ControllerTestUtils;
import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardType;
import com.potato.service.schedule.dto.request.ScheduleRequest;
import com.potato.service.schedule.dto.response.ScheduleResponse;
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
public class ScheduleControllerTest extends ControllerTestUtils {

    private ScheduleMockMvc scheduleMockMvc;

    @Autowired
    private AdminBoardRepository adminBoardRepository;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @BeforeEach
    void setUp() {
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
        //given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 1, 1, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 3, 1, 30);
        String title = "학사행정입니다.";
        AdminBoard adminBoard = AdminBoard.builder()
            .administratorId(testMember.getId())
            .title(title)
            .content("학사행정입니다.")
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();
        adminBoardRepository.save(adminBoard);

        LocalDate startDate = LocalDate.of(2021, 4, 1);
        LocalDate endDate = LocalDate.of(2021, 4, 30);
        ScheduleRequest request = ScheduleRequest.testInstance(startDate, endDate);

        //when
        ApiResponse<ScheduleResponse> response = scheduleMockMvc.retrieveScheduleBetween(request);

        //then
        assertThat(response.getData().getAdminBoards()).hasSize(1);
        assertThat(response.getData().getOrganizationBoards()).isEmpty();
        assertThat(response.getData().getAdminBoards().get(0).getTitle()).isEqualTo(title);
    }

    @Test
    void 사월_한달간의_스케줄을_불러올때_그룹_게시글도_불러온다() throws Exception {
        //given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 1, 1, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 3, 1, 30);
        String subDomain = "potato";
        String title = "title";
        OrganizationBoard organizationBoard = OrganizationBoard.builder()
            .title(title)
            .subDomain(subDomain)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .type(OrganizationBoardType.RECRUIT)
            .memberId(testMember.getId())
            .build();
        organizationBoardRepository.save(organizationBoard);

        LocalDate startDate = LocalDate.of(2021, 4, 1);
        LocalDate endDate = LocalDate.of(2021, 4, 30);
        ScheduleRequest request = ScheduleRequest.testInstance(startDate, endDate);

        //when
        ApiResponse<ScheduleResponse> response = scheduleMockMvc.retrieveScheduleBetween(request);

        //then
        assertThat(response.getData().getAdminBoards()).isEmpty();
        assertThat(response.getData().getOrganizationBoards()).hasSize(1);
        assertThat(response.getData().getOrganizationBoards().get(0).getTitle()).isEqualTo(title);
    }

    @Test
    void 게시글이_없을경우_빈배열을_반환한다() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2021, 4, 1);
        LocalDate endDate = LocalDate.of(2021, 4, 30);
        ScheduleRequest request = ScheduleRequest.testInstance(startDate, endDate);

        //when
        ApiResponse<ScheduleResponse> response = scheduleMockMvc.retrieveScheduleBetween(request);

        //then
        assertThat(response.getData().getOrganizationBoards()).isEmpty();
        assertThat(response.getData().getAdminBoards()).isEmpty();
    }

}
