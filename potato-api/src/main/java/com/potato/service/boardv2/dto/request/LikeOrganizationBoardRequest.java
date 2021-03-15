package com.potato.service.boardv2.dto.request;

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

}
