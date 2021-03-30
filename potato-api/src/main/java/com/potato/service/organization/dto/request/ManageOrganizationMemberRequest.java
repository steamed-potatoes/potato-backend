package com.potato.service.organization.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ManageOrganizationMemberRequest {

    @NotNull
    private Long targetMemberId;

    @Builder(builderMethodName = "testBuilder")
    public ManageOrganizationMemberRequest(@NotNull Long targetMemberId) {
        this.targetMemberId = targetMemberId;
    }

}
