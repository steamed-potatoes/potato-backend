package com.potato.service.board.organization.dto.request;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardCategory;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateOrganizationBoardRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String imageUrl;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;

    @NotNull
    private OrganizationBoardCategory type;

    @Builder(builderMethodName = "testBuilder")
    public CreateOrganizationBoardRequest(String title, String content, String imageUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, OrganizationBoardCategory type) {
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
            .category(type)
            .build();
    }

}
