package com.potato.service.schedule;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardCategory;
import com.potato.service.OrganizationMemberSetUpTest;
import com.potato.service.schedule.dto.request.ScheduleRequest;
import com.potato.service.schedule.dto.response.ScheduleResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ScheduleServiceTest extends OrganizationMemberSetUpTest {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @Autowired
    private AdminBoardRepository adminBoardRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAll();
        adminBoardRepository.deleteAll();
    }

    @Test
    void 특정_기간동안의_스케쥴을_불러오면_그룹_게시물도_포함된다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoard.builder()
            .subDomain("subDomain")
            .memberId(1L)
            .startDateTime(LocalDateTime.of(2021, 3, 5, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 7, 0, 0))
            .title("감자 신입회원 모집")
            .content("감자팀 신입회원을 모집합니다. 많은 참여바랍니다")
            .category(OrganizationBoardCategory.RECRUIT)
            .build();
        organizationBoardRepository.save(organizationBoard);

        ScheduleRequest request = ScheduleRequest.testInstance(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 3, 31));

        // when
        ScheduleResponse response = scheduleService.getDefaultSchedule(request);

        // then
        assertThat(response.getOrganizationBoards()).hasSize(1);
        assertThat(response.getAdminBoards()).isEmpty();
    }

    @Test
    void 특정_기간동안의_스케쥴을_불러오면_관리자_게시물도_포함된다() {
        // given
        AdminBoard adminBoard = AdminBoard.builder()
            .administratorId(1L)
            .title("학사일정")
            .content("학사일정 뭐시기")
            .startDateTime(LocalDateTime.of(2021, 3, 5, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 7, 0, 0))
            .build();

        adminBoardRepository.save(adminBoard);

        ScheduleRequest request = ScheduleRequest.testInstance(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 3, 31));

        // when
        ScheduleResponse response = scheduleService.getDefaultSchedule(request);

        // then
        assertThat(response.getAdminBoards()).hasSize(1);
        assertThat(response.getOrganizationBoards()).isEmpty();
    }

}
