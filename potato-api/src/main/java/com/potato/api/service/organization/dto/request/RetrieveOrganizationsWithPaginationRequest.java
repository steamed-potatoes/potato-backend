package com.potato.api.service.organization.dto.request;

import com.potato.domain.domain.organization.OrganizationCategory;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveOrganizationsWithPaginationRequest {

    private OrganizationCategory category;

    @Min(0)
    private long lastOrganizationId;

    @Min(1)
    @NotNull
    private int size;

    public static RetrieveOrganizationsWithPaginationRequest testInstance(OrganizationCategory category, long lastOrganizationId, int size) {
        return new RetrieveOrganizationsWithPaginationRequest(category, lastOrganizationId, size);
    }

}
