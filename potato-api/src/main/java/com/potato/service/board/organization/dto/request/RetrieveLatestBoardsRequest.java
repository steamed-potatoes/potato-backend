package com.potato.service.board.organization.dto.request;

import com.potato.domain.board.organization.OrganizationBoardCategory;
import lombok.*;

import javax.validation.constraints.Min;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveLatestBoardsRequest {

    @Min(0)
    private long lastOrganizationBoardId;

    @Min(1)
    private int size;

    private OrganizationBoardCategory type;

}
