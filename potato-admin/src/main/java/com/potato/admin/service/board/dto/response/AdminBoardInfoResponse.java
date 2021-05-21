package com.potato.admin.service.board.dto.response;

import com.potato.domain.domain.board.admin.AdminBoard;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminBoardInfoResponse {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    public static AdminBoardInfoResponse of(AdminBoard adminBoard) {
        return new AdminBoardInfoResponse(adminBoard.getId(), adminBoard.getTitle(), adminBoard.getContent(),
            adminBoard.getStartDateTime(), adminBoard.getEndDateTime());
    }

}
