package com.potato.api.controller.organization;

import com.potato.api.controller.ApiResponse;
import com.potato.api.controller.ControllerTestUtils;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.OrganizationCreator;
import com.potato.domain.domain.organization.OrganizationRepository;
import com.potato.common.exception.ErrorCode;
import com.potato.api.service.organization.dto.request.UpdateOrganizationInfoRequest;
import com.potato.api.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
public class OrganizationAdminControllerTest extends ControllerTestUtils {

    private OrganizationMockMvc organizationMockMvc;

    @Autowired
    private OrganizationRepository organizationRepository;

    private Organization organization;

    private String subDomain;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
        organizationMockMvc = new OrganizationMockMvc(mockMvc, objectMapper);
        subDomain = "potato";
        String name = "감자";
        String description = "개발의 감을 잡자";
        String profileUrl = "https://profile.com";
        organization = OrganizationCreator.create(subDomain, name, description, profileUrl, OrganizationCategory.NON_APPROVED_CIRCLE);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationRepository.deleteAll();
    }

    @Test
    void 그룹_관리자가_그룹의_정보를_수정하는_경우() throws Exception {
        //given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        String updateName = "감자화이팅";
        String updateDescription = "변경 감자 소개";
        String updateProfileUrl = "변경 후 대표 사진";

        UpdateOrganizationInfoRequest request = UpdateOrganizationInfoRequest.testBuilder()
            .name(updateName)
            .description(updateDescription)
            .profileUrl(updateProfileUrl)
            .build();

        //when
        ApiResponse<OrganizationInfoResponse> response = organizationMockMvc.updateOrganizationInfo(subDomain, request, token, 200);

        //then
        assertOrganizationInfoResponse(response.getData(), organization.getSubDomain(), updateName, updateDescription, updateProfileUrl, 1, organization.getCategory());
    }

    @Test
    void 그룹의_정보를_수정하려할때_그룹의_관리자가_아니면_수정할_수_없다() throws Exception {
        // given
        organization.addUser(testMember.getId());
        organizationRepository.save(organization);

        UpdateOrganizationInfoRequest request = UpdateOrganizationInfoRequest.testBuilder()
            .name("그룹 이름")
            .build();

        // when
        ApiResponse<OrganizationInfoResponse> response = organizationMockMvc.updateOrganizationInfo(subDomain, request, token, 403);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.FORBIDDEN_EXCEPTION.getCode());
    }

    private void assertOrganizationInfoResponse(OrganizationInfoResponse response, String subDomain, String name, String description, String profileUrl, int membersCount, OrganizationCategory category) {
        assertThat(response.getSubDomain()).isEqualTo(subDomain);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getDescription()).isEqualTo(description);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(response.getMembersCount()).isEqualTo(membersCount);
        assertThat(response.getCategory()).isEqualTo(category);
    }

}
