package com.potato.api.service.board.organization.dto.request;

import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import lombok.*;

import javax.validation.constraints.Min;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveLatestBoardsRequest {

    @Min(0)
    private long lastOrganizationBoardId;

    @Min(1)
    private int size;

    private OrganizationBoardCategory type;

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public RetrieveLatestBoardsRequest(long lastOrganizationBoardId, int size, OrganizationBoardCategory type) {
        this.lastOrganizationBoardId = lastOrganizationBoardId;
        this.size = size;
        this.type = type;
    }

}
