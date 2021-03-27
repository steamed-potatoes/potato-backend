package com.potato.service.schedule;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.service.schedule.dto.request.ScheduleRequest;
import com.potato.service.schedule.dto.response.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final OrganizationBoardRepository organizationBoardRepository;
    private final AdminBoardRepository adminBoardRepository;

    @Transactional(readOnly = true)
    public ScheduleResponse getDefaultSchedule(ScheduleRequest request) {
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findBetweenDate(request.getStartDate(), request.getEndDate());
        List<AdminBoard> adminBoardList = adminBoardRepository.findBetweenDate(request.getStartDate(), request.getEndDate());
        return ScheduleResponse.of(organizationBoardList, adminBoardList);
    }

}
