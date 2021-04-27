package com.potato.service.board.organization.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrievePopularOrganizationBoardRequest {

    @Min(1)
    @NotNull
    private int size;

}
