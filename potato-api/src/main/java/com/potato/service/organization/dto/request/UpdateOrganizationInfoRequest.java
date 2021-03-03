package com.potato.service.organization.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UpdateOrganizationInfoRequest {

    @NotBlank
    private String name;

    private String description;

    private String profileUrl;

    @Builder(builderMethodName = "testBuilder")
    public UpdateOrganizationInfoRequest(String name, String description, String profileUrl) {
        this.name = name;
        this.description = description;
        this.profileUrl = profileUrl;
    }

}
