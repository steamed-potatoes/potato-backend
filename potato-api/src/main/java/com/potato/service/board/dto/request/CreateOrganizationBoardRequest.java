package com.potato.service.board.dto.request;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardType;
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
public class CreateOrganizationBoardRequest {

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
    public CreateOrganizationBoardRequest(String title, String content, String imageUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, OrganizationBoardType type) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.type = type;
    }

    public OrganizationBoard toEntity(String subDomain, Long memberId) {
        return OrganizationBoard.builder()
            .subDomain(subDomain)
            .memberId(memberId)
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .type(type)
            .build();
    }

}
