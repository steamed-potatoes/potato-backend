package com.potato.service.board.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateAdminBoardRequest {

    @NotNull
    private Long adminBoardId;

    @NotBlank
    private String title;

    private String content;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    @Builder(builderMethodName = "testBuilder")
    public UpdateAdminBoardRequest(@NotNull Long adminBoardId, @NotBlank String title, String content, @NotNull LocalDateTime startDateTime, @NotNull LocalDateTime endDateTime) {
        this.adminBoardId = adminBoardId;
        this.title = title;
        this.content = content;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

}
