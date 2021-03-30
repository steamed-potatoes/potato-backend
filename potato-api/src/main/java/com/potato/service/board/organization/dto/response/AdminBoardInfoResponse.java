package com.potato.service.board.organization.dto.response;

import com.potato.domain.board.admin.AdminBoard;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminBoardInfoResponse {

    private Long id;

    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String content;

    public static AdminBoardInfoResponse of(AdminBoard adminBoard) {
        return new AdminBoardInfoResponse(adminBoard.getId(), adminBoard.getTitle(),
            adminBoard.getStartDateTime(), adminBoard.getEndDateTime(), adminBoard.getContent());
    }

}
