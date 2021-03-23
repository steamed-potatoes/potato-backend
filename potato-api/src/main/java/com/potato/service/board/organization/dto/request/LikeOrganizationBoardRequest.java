package com.potato.service.board.organization.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
public class LikeOrganizationBoardRequest {

    @NotNull
    private Long organizationBoardId;

    @NotNull
    private Boolean isLike;

    private LikeOrganizationBoardRequest(@NotNull Long organizationBoardId, boolean isLike) {
        this.organizationBoardId = organizationBoardId;
        this.isLike = isLike;
    }

    public static LikeOrganizationBoardRequest testInstance(Long organizationBoardId, boolean isLike) {
        return new LikeOrganizationBoardRequest(organizationBoardId, isLike);
    }

}
