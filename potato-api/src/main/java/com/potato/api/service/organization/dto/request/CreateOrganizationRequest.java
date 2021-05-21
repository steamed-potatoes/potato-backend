package com.potato.api.service.organization.dto.request;

import com.potato.domain.domain.organization.Organization;
import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateOrganizationRequest {

    @NotBlank
    private String subDomain;

    @NotBlank
    private String name;

    private String description;

    private String profileUrl;

    @Builder(builderMethodName = "testBuilder")
    public CreateOrganizationRequest(@NotBlank String subDomain, @NotBlank String name, String description, String profileUrl) {
        this.subDomain = subDomain;
        this.name = name;
        this.description = description;
        this.profileUrl = profileUrl;
    }

    public Organization toEntity() {
        return Organization.defaultInstance(subDomain, name, description, profileUrl);
    }

}
