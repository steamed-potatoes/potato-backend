package com.potato.api.controller.organization;

import com.potato.api.controller.ApiResponse;
import com.potato.api.controller.AbstractControllerTest;
import com.potato.api.controller.organization.api.OrganizationFollowerMockMvc;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.OrganizationCreator;
import com.potato.domain.domain.organization.OrganizationRepository;
import com.potato.common.exception.ErrorCode;
import com.potato.api.service.member.dto.response.MemberInfoResponse;
import com.potato.api.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.potato.api.helper.member.MemberTestHelper.assertMemberInfoResponse;
import static com.potato.api.helper.organization.OrganizationServiceTestUtils.assertOrganizationInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;

class OrganizationFollowControllerTest extends AbstractControllerTest {

    private OrganizationFollowerMockMvc organizationMockMvc;

    @Autowired
    private OrganizationRepository organizationRepository;

    private Organization organization;
    private String subDomain;
    private String name;
    private String description;
    private String profileUrl;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
        organizationMockMvc = new OrganizationFollowerMockMvc(mockMvc, objectMapper);
        subDomain = "potato";
        name = "감자";
        description = "개발의 감을 잡자";
        profileUrl = "https://profile.com";
        organization = OrganizationCreator.create(subDomain, name, description, profileUrl, OrganizationCategory.NON_APPROVED_CIRCLE);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationRepository.deleteAll();
    }

    @Test
    void 특정_조직을_팔로우하려는_경우() throws Exception {
        //given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        //when
        ApiResponse<String> response = organizationMockMvc.followOrganization(subDomain, token, 200);

        //then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @Test
    void 특정_조직을_팔로우_취소하는_경우() throws Exception {
        //given
        organization.addAdmin(testMember.getId());
        organization.addFollow(testMember.getId());
        organizationRepository.save(organization);

        //when
        ApiResponse<String> response = organizationMockMvc.unFollowOrganization(subDomain, token, 200);

        //then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @Test
    void 특정_조직_팔로우하지_않은_사람이_팔로우_취소하는_경우_애러발생() throws Exception {
        //given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        //when
        ApiResponse<String> response = organizationMockMvc.unFollowOrganization(subDomain, token, 404);

        //then
        assertThat(response.getCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION.getCode());
    }

    @Test
    void 그룹을_팔로우한_멤버_리스트를_불러오는_경우() throws Exception {
        //given
        organization.addAdmin(testMember.getId());
        organization.addFollow(testMember.getId());
        organizationRepository.save(organization);

        //when
        ApiResponse<List<MemberInfoResponse>> response = organizationMockMvc.getOrganizationFollowMember(subDomain, 200);

        //then
        assertThat(response.getData()).hasSize(1);
        assertMemberInfoResponse(response.getData().get(0), testMember.getEmail(), testMember.getName(), testMember.getProfileUrl(), testMember.getMajor(), testMember.getClassNumber());
    }

    @Test
    void 내가_팔로우한_그룹들을_가져오는_경우() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        organization.addFollow(testMember.getId());
        organizationRepository.save(organization);

        // when
        ApiResponse<List<OrganizationInfoResponse>> response = organizationMockMvc.retrieveFollowingOrganization(token, 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertOrganizationInfoResponse(response.getData().get(0), subDomain, name, description, profileUrl, organization.getCategory(), 1);
        assertThat(response.getData().get(0).getSubDomain()).isEqualTo(subDomain);
    }

}
