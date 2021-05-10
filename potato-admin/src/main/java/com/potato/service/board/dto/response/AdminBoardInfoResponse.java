package com.potato.service.board.dto.response;

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

    private String content;

    private String imageUrl;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    public static AdminBoardInfoResponse of(AdminBoard adminBoard) {
        return new AdminBoardInfoResponse(adminBoard.getId(), adminBoard.getTitle(), adminBoard.getContent(), adminBoard.getImageUrl(),
            adminBoard.getStartDateTime(), adminBoard.getEndDateTime());
    }

}
