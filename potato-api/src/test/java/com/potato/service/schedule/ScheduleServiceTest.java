package com.potato.service.schedule;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardType;
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

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAll();
    }

    @Test
    void 특정_기간동안의_스케쥴을_불러온다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoard.builder()
            .subDomain("subDomain")
            .memberId(1L)
            .startDateTime(LocalDateTime.of(2021, 3, 5, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 7, 0, 0))
            .title("감자 신입회원 모집")
            .content("감자팀 신입회원을 모집합니다. 많은 참여바랍니다")
            .type(OrganizationBoardType.RECRUIT)
            .build();
        organizationBoardRepository.save(organizationBoard);

        ScheduleRequest request = ScheduleRequest.testInstance(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 3, 31));

        // when
        ScheduleResponse response = scheduleService.getDefaultSchedule(request);

        // then
        assertThat(response.getOrganizationBoard()).hasSize(1);
    }

}
