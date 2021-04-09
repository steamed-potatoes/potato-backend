package com.potato.service.organization.dto.request;

import com.potato.domain.organization.OrganizationCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateCategoryRequest {

    @NotNull
    OrganizationCategory category;

    private UpdateCategoryRequest(@NotNull OrganizationCategory category) {
        this.category = category;
    }

    public static UpdateCategoryRequest testInstance(OrganizationCategory category) {
        return new UpdateCategoryRequest(category);
    }

}
