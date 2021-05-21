package com.potato.api.service.organization;

import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.OrganizationMemberMapper;
import com.potato.domain.domain.organization.OrganizationRole;
import com.potato.api.service.organization.dto.response.OrganizationInfoResponse;

import static org.assertj.core.api.Assertions.assertThat;

final class OrganizationServiceTestUtils {

    static void assertOrganization(Organization organization, String subDomain, String name, String description, String profileUrl) {
        assertThat(organization.getSubDomain()).isEqualTo(subDomain);
        assertThat(organization.getName()).isEqualTo(name);
        assertThat(organization.getDescription()).isEqualTo(description);
        assertThat(organization.getProfileUrl()).isEqualTo(profileUrl);
    }

    static void assertOrganizationMemberMapper(OrganizationMemberMapper organizationMemberMapper, Long memberId, OrganizationRole role) {
        assertThat(organizationMemberMapper.getMemberId()).isEqualTo(memberId);
        assertThat(organizationMemberMapper.getRole()).isEqualTo(role);
    }

    static void assertOrganizationInfoResponse(OrganizationInfoResponse response, String subDomain, String name,
                                               String description, String profileUrl, OrganizationCategory category, int membersCount) {
        assertThat(response.getSubDomain()).isEqualTo(subDomain);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getDescription()).isEqualTo(description);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(response.getCategory()).isEqualTo(category);
        assertThat(response.getMembersCount()).isEqualTo(membersCount);
    }

    static void assertOrganizationInfoResponse(OrganizationInfoResponse organizationInfoResponse, String subDomain) {
        assertThat(organizationInfoResponse.getSubDomain()).isEqualTo(subDomain);
    }

}
