package com.potato.service.organization.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ApplyOrganizationMemberRequest {

    private Long targetMemberId;

    @Builder(builderMethodName = "testBuilder")
    public ApplyOrganizationMemberRequest(@NotBlank Long targetMemberId) {
        this.targetMemberId = targetMemberId;
    }

}
