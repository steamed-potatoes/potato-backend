package com.potato.api.helper.organization;

import com.potato.api.service.organization.dto.response.MemberInOrganizationResponse;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.OrganizationMemberMapper;
import com.potato.domain.domain.organization.OrganizationRole;
import com.potato.api.service.organization.dto.response.OrganizationInfoResponse;

import static org.assertj.core.api.Assertions.assertThat;

public final class OrganizationServiceTestUtils {

    public static void assertOrganization(Organization organization, String subDomain, String name, String description, String profileUrl) {
        assertThat(organization.getSubDomain()).isEqualTo(subDomain);
        assertThat(organization.getName()).isEqualTo(name);
        assertThat(organization.getDescription()).isEqualTo(description);
        assertThat(organization.getProfileUrl()).isEqualTo(profileUrl);
    }

    public static void assertOrganizationMemberMapper(OrganizationMemberMapper organizationMemberMapper, Long memberId, OrganizationRole role) {
        assertThat(organizationMemberMapper.getMemberId()).isEqualTo(memberId);
        assertThat(organizationMemberMapper.getRole()).isEqualTo(role);
    }

    public static void assertOrganizationInfoResponse(OrganizationInfoResponse response, String subDomain, String name,
                                                      String description, String profileUrl, OrganizationCategory category, int membersCount) {
        assertThat(response.getSubDomain()).isEqualTo(subDomain);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getDescription()).isEqualTo(description);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(response.getCategory()).isEqualTo(category);
        assertThat(response.getMembersCount()).isEqualTo(membersCount);
    }

    public static void assertOrganizationInfoResponse(OrganizationInfoResponse organizationInfoResponse, String subDomain) {
        assertThat(organizationInfoResponse.getSubDomain()).isEqualTo(subDomain);
    }

    public static void assertMemberInOrganizationResponse(MemberInOrganizationResponse response, Long memberId, String email, String name, String profileUrl, OrganizationRole role) {
        assertThat(response.getMemberId()).isEqualTo(memberId);
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(response.getRole()).isEqualTo(role);
    }

}
