package com.potato.service.board.organization.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeOrganizationBoardRequest {

    @Min(0)
    private long organizationBoardId;

    private LikeOrganizationBoardRequest(long organizationBoardId) {
        this.organizationBoardId = organizationBoardId;
    }

    public static LikeOrganizationBoardRequest testInstance(Long organizationBoardId) {
        return new LikeOrganizationBoardRequest(organizationBoardId);
    }

}
