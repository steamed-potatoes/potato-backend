package com.potato.service.schedule;

import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.service.schedule.dto.request.ScheduleRequest;
import com.potato.service.schedule.dto.response.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final OrganizationBoardRepository organizationBoardRepository;
    private final AdminBoardRepository adminBoardRepository;

    @Transactional(readOnly = true)
    public ScheduleResponse getDefaultSchedule(ScheduleRequest request) {
        return ScheduleResponse.of(
            organizationBoardRepository.findAllBetweenDate(request.getStartDate(), request.getEndDate()),
            adminBoardRepository.findAllBetweenDate(request.getStartDate(), request.getEndDate())
        );
    }

}
