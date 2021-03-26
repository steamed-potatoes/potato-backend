package com.potato.controller.organization;

import com.potato.controller.ApiResponse;
import com.potato.controller.ControllerTestUtils;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCategory;
import com.potato.domain.organization.OrganizationCreator;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import com.potato.service.organization.dto.response.OrganizationWithMembersInfoResponse;
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
class OrganizationControllerTest extends ControllerTestUtils {

    private OrganizationMockMvc organizationMockMvc;

    @Autowired
    private OrganizationRepository organizationRepository;

    @BeforeEach
    void setUp() {
        super.setup();
        organizationMockMvc = new OrganizationMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationRepository.deleteAll();
    }

    @Test
    void 새로운_그룹을_생성한다() throws Exception {
        // given
        String subDomain = "potato";
        String name = "감자";
        String description = "개발의 감을 잡자";
        String profileUrl = "http://profile.com";
        String token = memberMockMvc.getMockMemberToken();

        CreateOrganizationRequest request = CreateOrganizationRequest.testBuilder()
            .subDomain(subDomain)
            .name(name)
            .description(description)
            .profileUrl(profileUrl)
            .build();

        // when
        ApiResponse<OrganizationInfoResponse> response = organizationMockMvc.createOrganization(request, token);

        // then
        assertThat(response.getData().getSubDomain()).isEqualTo(subDomain);
        assertThat(response.getData().getName()).isEqualTo(name);
        assertThat(response.getData().getProfileUrl()).isEqualTo(profileUrl);
        assertThat(response.getData().getDescription()).isEqualTo(description);
        assertThat(response.getData().getMembersCount()).isEqualTo(1);
    }

    @Test
    void 그룹의_정보를_조회한다() throws Exception {
        // given
        String subDomain = "potato";
        String name = "감자";
        String description = "개발의 감을 잡자";
        String profileUrl = "http://profile.com";

        Organization organization = OrganizationCreator.create(subDomain, name, description, profileUrl, OrganizationCategory.NON_APPROVED_CIRCLE);
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        // when
        ApiResponse<OrganizationWithMembersInfoResponse> response = organizationMockMvc.getDetailOrganizationInfo(subDomain);

        // then
        assertThat(response.getData().getOrganization().getSubDomain()).isEqualTo(subDomain);
        assertThat(response.getData().getOrganization().getName()).isEqualTo(name);
        assertThat(response.getData().getOrganization().getProfileUrl()).isEqualTo(profileUrl);
        assertThat(response.getData().getOrganization().getDescription()).isEqualTo(description);
        assertThat(response.getData().getOrganization().getMembersCount()).isEqualTo(1);

        assertThat(response.getData().getMembers()).hasSize(1);
        assertThat(response.getData().getMembers().get(0).getId()).isEqualTo(testMember.getId());
        assertThat(response.getData().getMembers().get(0).getEmail()).isEqualTo(testMember.getEmail());
        assertThat(response.getData().getMembers().get(0).getName()).isEqualTo(testMember.getName());
        assertThat(response.getData().getMembers().get(0).getProfileUrl()).isEqualTo(testMember.getProfileUrl());
    }

    @Test
    void 그룹_리스트를_조회한다() throws Exception {
        // given
        String subDomain = "potato";
        String name = "감자";
        String description = "개발의 감을 잡자";
        String profileUrl = "http://profile.com";

        Organization organization = OrganizationCreator.create(subDomain, name, description, profileUrl, OrganizationCategory.NON_APPROVED_CIRCLE);
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        // when
        ApiResponse<List<OrganizationInfoResponse>> response = organizationMockMvc.getOrganizations();

        // then
        assertThat(response.getData()).hasSize(1);
        assertThat(response.getData().get(0).getSubDomain()).isEqualTo(subDomain);
        assertThat(response.getData().get(0).getName()).isEqualTo(name);
        assertThat(response.getData().get(0).getDescription()).isEqualTo(description);
        assertThat(response.getData().get(0).getProfileUrl()).isEqualTo(profileUrl);
        assertThat(response.getData().get(0).getMembersCount()).isEqualTo(1);
    }

}
