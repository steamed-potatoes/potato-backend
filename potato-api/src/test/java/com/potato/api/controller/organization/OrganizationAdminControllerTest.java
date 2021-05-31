package com.potato.api.controller.organization;

import com.potato.api.controller.ApiResponse;
import com.potato.api.controller.AbstractControllerTest;
import com.potato.api.controller.organization.api.OrganizationAdminMockMvc;
import com.potato.api.service.organization.dto.request.AppointOrganizationAdminRequest;
import com.potato.api.service.organization.dto.request.ManageOrganizationMemberRequest;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.OrganizationCreator;
import com.potato.domain.domain.organization.OrganizationRepository;
import com.potato.common.exception.ErrorCode;
import com.potato.api.service.organization.dto.request.UpdateOrganizationInfoRequest;
import com.potato.api.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.potato.api.helper.organization.OrganizationServiceTestUtils.assertOrganizationInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;

class OrganizationAdminControllerTest extends AbstractControllerTest {

    private OrganizationAdminMockMvc organizationMockMvc;

    @Autowired
    private OrganizationRepository organizationRepository;

    private Organization organization;

    private String subDomain;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
        organizationMockMvc = new OrganizationAdminMockMvc(mockMvc, objectMapper);
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

    @DisplayName("PUT /api/v1/organization 200 OK")
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
        assertOrganizationInfoResponse(response.getData(), organization.getSubDomain(), updateName, updateDescription, updateProfileUrl, organization.getCategory(), 1);
    }

    @DisplayName("PUT /api/v1/organization 403 FORBIDDEN")
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

    @DisplayName("PUT /api/v1/organization/join/approve 200 OK")
    @Test
    void 그룹_관리자가_가입_신청을_수락한다() throws Exception {
        // given
        Long targetMemberId = 100L;

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(testMember.getId());
        organization.addPending(targetMemberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.of(targetMemberId);

        // when
        ApiResponse<String> response = organizationMockMvc.approveOrganizationMember(subDomain, request, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @DisplayName("PUT /api/v1/organization/join/deny 200 OK")
    @Test
    void 그룹_관리자가_가입_신청을_거절한다() throws Exception {
        // given
        Long targetMemberId = 100L;

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(testMember.getId());
        organization.addPending(targetMemberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.of(targetMemberId);

        // when
        ApiResponse<String> response = organizationMockMvc.denyOrganizationMember(subDomain, request, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @DisplayName("PUT /api/v1/organization/kick 200 OK")
    @Test
    void 그룹_관리자가_일반_멤버를_강퇴시키는_API() throws Exception {
        // given
        Long targetMemberId = 100L;

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(testMember.getId());
        organization.addUser(targetMemberId);
        organizationRepository.save(organization);

        // when
        ApiResponse<String> response = organizationMockMvc.kickOffOrganizationUserByAdmin(subDomain, targetMemberId, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @DisplayName("PUT /api/v1/organization/appoint 200 OK")
    @Test
    void 그룹_관리자가_관리자를_넘겨주는_API() throws Exception {
        // given
        Long targetMemberId = 100L;

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(testMember.getId());
        organization.addUser(targetMemberId);
        organizationRepository.save(organization);

        AppointOrganizationAdminRequest request = AppointOrganizationAdminRequest.testInstance(targetMemberId);

        // when
        ApiResponse<String> response = organizationMockMvc.appointOrganizationAdmin(subDomain, request, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

}
