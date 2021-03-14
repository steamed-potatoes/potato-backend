package com.potato.service.boardv2.dto.request;

import com.potato.domain.boardV2.organization.OrganizationBoard;
import com.potato.domain.boardV2.organization.OrganizationBoardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class CreateOrganizationBoardRequest {

    private String title;

    private String content;

    private String imageUrl;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

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
