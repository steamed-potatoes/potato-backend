package com.potato.service.organization.dto.response;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCategory;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationInfoResponse {

    private Long id;

    private String subDomain;

    private String name;

    private String description;

    private String profileUrl;

    private int membersCount;

    private OrganizationCategory category;

    public static OrganizationInfoResponse of(Organization organization) {
        return new OrganizationInfoResponse(organization.getId(), organization.getSubDomain(), organization.getName(),
            organization.getDescription(), organization.getProfileUrl(), organization.getMembersCount(), organization.getCategory());
    }

}
