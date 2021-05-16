package com.potato.service.organization.dto.response;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCategory;
import com.potato.service.common.dto.response.BaseTimeResponse;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationInfoResponse extends BaseTimeResponse {

    private String subDomain;

    private String name;

    private String description;

    private String profileUrl;

    private int membersCount;

    private OrganizationCategory category;

    @Builder
    private OrganizationInfoResponse(String subDomain, String name, String description, String profileUrl,
                                     int membersCount, OrganizationCategory category) {
        this.subDomain = subDomain;
        this.name = name;
        this.description = description;
        this.profileUrl = profileUrl;
        this.membersCount = membersCount;
        this.category = category;
    }

    public static OrganizationInfoResponse of(Organization organization) {
        OrganizationInfoResponse response = OrganizationInfoResponse.builder()
            .subDomain(organization.getSubDomain())
            .name(organization.getName())
            .description(organization.getDescription())
            .profileUrl(organization.getProfileUrl())
            .membersCount(organization.getMembersCount())
            .category(organization.getCategory())
            .build();
        response.setBaseTime(organization);
        return response;
    }

}
