package com.potato.service.board.organization.dto.response;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.service.common.dto.response.BaseTimeResponse;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminBoardInfoResponse extends BaseTimeResponse {

    private Long id;

    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String content;

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
