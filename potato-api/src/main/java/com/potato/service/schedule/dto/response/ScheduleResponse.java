package com.potato.service.schedule.dto.response;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.service.board.dto.response.OrganizationBoardInfoResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleResponse {

    private final List<OrganizationBoardInfoResponse> organizationBoard;

    public static ScheduleResponse of(List<OrganizationBoard> organizationBoardList) {
        List<OrganizationBoardInfoResponse> organizationBoards = organizationBoardList.stream()
            .map(OrganizationBoardInfoResponse::of)
            .collect(Collectors.toList());
        return new ScheduleResponse(organizationBoards);
    }

}
