package com.potato.service.board.organization.dto.request;

import com.potato.domain.board.organization.OrganizationBoardType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveLatestBoardsRequest {

    @Min(0)
    private long lastOrganizationBoardId;

    @Min(1)
    private int size;

    private OrganizationBoardType type;

}
