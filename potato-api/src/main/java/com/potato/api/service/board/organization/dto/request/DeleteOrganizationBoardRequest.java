package com.potato.api.service.board.organization.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteOrganizationBoardRequest {

    @NotNull
    private Long organizationBoardId;

}
