package com.potato.service.board.organization.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
public class DeleteOrganizationBoardRequest {

    @NotNull
    private Long organizationBoardId;

}
