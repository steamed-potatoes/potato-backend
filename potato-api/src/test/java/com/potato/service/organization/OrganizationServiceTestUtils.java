package com.potato.service.organization;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCategory;
import com.potato.domain.organization.OrganizationMemberMapper;
import com.potato.domain.organization.OrganizationRole;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;

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

    static void assertOrganizationInfoResponse(OrganizationInfoResponse response, Long id, String subDomain, String name,
                                               String description, String profileUrl, OrganizationCategory category, int membersCount) {
        assertThat(response.getId()).isEqualTo(id);
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
