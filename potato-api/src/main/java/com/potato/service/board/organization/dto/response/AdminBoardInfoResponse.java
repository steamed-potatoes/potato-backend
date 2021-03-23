package com.potato.service.board.organization.dto.response;

import com.potato.domain.board.admin.AdminBoard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminBoardInfoResponse {

    private final Long id;

    private final String title;

    private final LocalDateTime startDateTime;

    private final LocalDateTime endDateTime;

    private final String content;

    public static AdminBoardInfoResponse of(AdminBoard adminBoard) {
        return new AdminBoardInfoResponse(adminBoard.getId(), adminBoard.getTitle(),
            adminBoard.getStartDateTime(), adminBoard.getEndDateTime(), adminBoard.getContent());
    }

}
