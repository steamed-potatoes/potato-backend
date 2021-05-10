package com.potato.service.board.organization.dto.request;

import lombok.*;

import javax.validation.constraints.Min;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeOrganizationBoardRequest {

    @Min(0)
    private long organizationBoardId;

    public static LikeOrganizationBoardRequest testInstance(Long organizationBoardId) {
        return new LikeOrganizationBoardRequest(organizationBoardId);
    }

}
