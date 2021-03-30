package com.potato.service.schedule.dto.response;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.service.board.organization.dto.response.AdminBoardInfoResponse;
import com.potato.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleResponse {

    private List<OrganizationBoardInfoResponse> organizationBoards;
    private List<AdminBoardInfoResponse> adminBoards;

    public static ScheduleResponse of(List<OrganizationBoard> organizationBoardList, List<AdminBoard> adminBoardList) {
        List<OrganizationBoardInfoResponse> organizationBoards = organizationBoardList.stream()
            .map(OrganizationBoardInfoResponse::of)
            .collect(Collectors.toList());
        List<AdminBoardInfoResponse> adminBoardInfoResponses = adminBoardList.stream()
            .map(AdminBoardInfoResponse::of)
            .collect(Collectors.toList());
        return new ScheduleResponse(organizationBoards, adminBoardInfoResponses);
    }

}
