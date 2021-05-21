package com.potato.admin.service.organization.dto.request;

import com.potato.domain.domain.organization.OrganizationCategory;
import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateCategoryRequest {

    @NotNull
    private OrganizationCategory category;

    public static UpdateCategoryRequest testInstance(OrganizationCategory category) {
        return new UpdateCategoryRequest(category);
    }

}
