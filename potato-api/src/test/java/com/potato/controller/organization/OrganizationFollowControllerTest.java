package com.potato.controller.organization;

import com.potato.controller.ApiResponse;
import com.potato.controller.ControllerTestUtils;
import com.potato.domain.organization.*;
import com.potato.exception.ErrorCode;
import com.potato.service.member.dto.response.MemberInfoResponse;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
public class OrganizationFollowControllerTest extends ControllerTestUtils {

    private OrganizationMockMvc organizationMockMvc;

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
        organizationMockMvc = new OrganizationMockMvc(mockMvc, objectMapper);
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
        assertMemberInfoResponse(response.getData().get(0), testMember.getEmail(), testMember.getName(), testMember.getMajorName(), testMember.getClassNumber(), testMember.getProfileUrl());
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
        assertOrganizationInfoResponse(response.getData().get(0), subDomain, name, description, profileUrl, 1, organization.getCategory());
        assertThat(response.getData().get(0).getSubDomain()).isEqualTo(subDomain);
    }

    private void assertOrganizationInfoResponse(OrganizationInfoResponse response, String subDomain, String name, String description, String profileUrl, int membersCount, OrganizationCategory category) {
        assertThat(response.getSubDomain()).isEqualTo(subDomain);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getDescription()).isEqualTo(description);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(response.getMembersCount()).isEqualTo(membersCount);
        assertThat(response.getCategory()).isEqualTo(category);
    }

    private void assertMemberInfoResponse(MemberInfoResponse response, String email, String name, String major, int classNumber, String profileUrl) {
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getMajor()).isEqualTo(major);
        assertThat(response.getClassNumber()).isEqualTo(classNumber);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
    }

}
