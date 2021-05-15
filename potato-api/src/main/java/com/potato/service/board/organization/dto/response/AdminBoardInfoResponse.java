package com.potato.service.board.organization.dto.response;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.service.common.dto.response.BaseTimeResponse;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
public class AdminBoardInfoResponse extends BaseTimeResponse {

    private final Long id;

    private final String title;

    private final LocalDateTime startDateTime;

    private final LocalDateTime endDateTime;

    private final String content;

    @Builder
    private AdminBoardInfoResponse(Long id, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String content) {
        this.id = id;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.content = content;
    }

    public static AdminBoardInfoResponse of(AdminBoard adminBoard) {
        AdminBoardInfoResponse response = AdminBoardInfoResponse.builder()
            .id(adminBoard.getId())
            .title(adminBoard.getTitle())
            .startDateTime(adminBoard.getStartDateTime())
            .endDateTime(adminBoard.getEndDateTime())
            .content(adminBoard.getContent())
            .build();
        response.setBaseTime(adminBoard);
        return response;
    }

}
