package com.potato.api.service.board.organization.dto.request;

import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateOrganizationBoardRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private List<String> imageUrlList;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;

    @NotNull
    private OrganizationBoardCategory type;

    @NotNull
    private List<String> hashTags;

    @Builder(builderMethodName = "testBuilder")
    public CreateOrganizationBoardRequest(String title, String content, List<String> imageUrlList, LocalDateTime startDateTime,
                                          LocalDateTime endDateTime, OrganizationBoardCategory type, List<String> hashTags) {
        this.title = title;
        this.content = content;
        this.imageUrlList = imageUrlList;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.type = type;
        this.hashTags = hashTags;
    }

    public OrganizationBoard toEntity(String subDomain, Long memberId) {
        return OrganizationBoard.builder()
            .subDomain(subDomain)
            .memberId(memberId)
            .title(title)
            .content(content)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .category(type)
            .build();
    }

}
