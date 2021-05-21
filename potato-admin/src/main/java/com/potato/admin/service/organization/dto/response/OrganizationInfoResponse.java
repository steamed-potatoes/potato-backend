package com.potato.admin.service.organization.dto.response;

import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationInfoResponse {

    private final Long id;

    private final String subDomain;

    private final String name;

    private final String description;

    private final String profileUrl;

    private final int membersCount;

    private final OrganizationCategory category;

    public static OrganizationInfoResponse of(Organization organization) {
        return new OrganizationInfoResponse(organization.getId(), organization.getSubDomain(), organization.getName(), organization.getDescription(),
            organization.getProfileUrl(), organization.getMembersCount(), organization.getCategory());
    }

}
