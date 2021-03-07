package com.potato.service.organization.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
public class ManageOrganizationMemberRequest {

    @NotNull
    private Long targetMemberId;

    @Builder(builderMethodName = "testBuilder")
    public ManageOrganizationMemberRequest(@NotNull Long targetMemberId) {
        this.targetMemberId = targetMemberId;
    }

}
