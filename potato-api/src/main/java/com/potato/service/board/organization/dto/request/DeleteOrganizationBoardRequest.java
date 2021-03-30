package com.potato.service.board.organization.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteOrganizationBoardRequest {

    @NotNull
    private Long organizationBoardId;

}
