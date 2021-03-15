package com.potato.service.boardv2.dto.request;

import com.potato.domain.boardV2.organization.OrganizationBoardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class UpdateOrganizationBoardRequest {

    @NotNull
    private Long organizationBoardId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String imageUrl;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
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
