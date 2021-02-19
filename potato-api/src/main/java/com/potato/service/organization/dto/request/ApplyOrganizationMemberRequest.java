package com.potato.service.organization.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplyOrganizationMemberRequest {

    private String subDomain;

    private Long targetMemberId;

}
