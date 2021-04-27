package com.potato.service.board.organization.dto.request;

import com.potato.domain.board.organization.OrganizationBoardType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateOrganizationBoardRequest {

    @NotNull
    private Long organizationBoardId;

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

    @NotNull
    private OrganizationBoardType type;

    @Builder(builderMethodName = "testBuilder")
    public UpdateOrganizationBoardRequest(Long organizationBoardId, String title, String content, String imageUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, OrganizationBoardType type) {
        this.organizationBoardId = organizationBoardId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.type = type;
    }

}
