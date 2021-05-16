package com.potato.service.organization.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ManageOrganizationMemberRequest {

    @NotNull
    private Long targetMemberId;

    public static ManageOrganizationMemberRequest of(Long targetMemberId) {
        return new ManageOrganizationMemberRequest(targetMemberId);
    }

}
