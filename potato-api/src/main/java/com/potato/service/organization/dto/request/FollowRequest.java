package com.potato.service.organization.dto.request;

import com.potato.domain.organization.Organization;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class FollowRequest {

    @NotBlank
    private String subDomain;

    @Builder(builderMethodName = "testBuilder")
    public FollowRequest(@NotBlank String subDomain) {
        this.subDomain = subDomain;
    }

}
