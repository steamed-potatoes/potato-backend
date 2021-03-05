package com.potato.service.organization.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class FollowRequest {

    @NotBlank
    private String subDomain;

    public FollowRequest(@NotBlank String subDomain) {
        this.subDomain = subDomain;
    }

    public static FollowRequest testInstance(String subDomain) {
        return new FollowRequest(subDomain);
    }

}
