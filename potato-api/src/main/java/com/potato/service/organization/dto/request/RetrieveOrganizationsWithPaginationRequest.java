package com.potato.service.organization.dto.request;

import com.potato.domain.organization.OrganizationCategory;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveOrganizationsWithPaginationRequest {

    private OrganizationCategory category;

    @Min(1)
    @NotNull
    private int size;

}
