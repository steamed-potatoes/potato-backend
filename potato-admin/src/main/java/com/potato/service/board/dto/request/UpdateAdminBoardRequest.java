package com.potato.service.board.dto.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    private String imageUrl;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;

    @Builder(builderMethodName = "testBuilder")
    public UpdateAdminBoardRequest(@NotNull Long adminBoardId, @NotBlank String title, String content, String imageUrl, @NotNull LocalDateTime startDateTime, @NotNull LocalDateTime endDateTime) {
        this.adminBoardId = adminBoardId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

}
