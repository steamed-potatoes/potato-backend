package com.potato.service.board.dto.request;

import com.potato.domain.board.admin.AdminBoard;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateAdminBoardRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    @Builder(builderMethodName = "testBuilder")
    public CreateAdminBoardRequest(@NotBlank String title, @NotBlank String content, @NotBlank LocalDateTime startDateTime, @NotBlank LocalDateTime endDateTime) {
        this.title = title;
        this.content = content;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public AdminBoard toEntity(Long administratorId) {
        return AdminBoard.builder()
            .administratorId(administratorId)
            .title(title)
            .content(content)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();
    }

}
