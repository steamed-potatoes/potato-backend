package com.potato.admin.service.board.dto.request;

import com.potato.domain.domain.board.admin.AdminBoard;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateAdminBoardRequest {

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
    public CreateAdminBoardRequest(@NotBlank String title, @NotBlank String content, String imageUrl, @NotBlank LocalDateTime startDateTime, @NotBlank LocalDateTime endDateTime) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public AdminBoard toEntity(Long administratorId) {
        return AdminBoard.builder()
            .administratorId(administratorId)
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();
    }

}
