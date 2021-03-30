package com.potato.service.board.organization.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeOrganizationBoardRequest {

    @NotNull
    private Long organizationBoardId;

    private LikeOrganizationBoardRequest(@NotNull Long organizationBoardId) {
        this.organizationBoardId = organizationBoardId;
    }

    public static LikeOrganizationBoardRequest testInstance(Long organizationBoardId) {
        return new LikeOrganizationBoardRequest(organizationBoardId);
    }

}
