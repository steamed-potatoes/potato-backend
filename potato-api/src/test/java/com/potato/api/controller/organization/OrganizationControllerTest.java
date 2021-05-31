package com.potato.api.controller.organization;

import com.potato.api.controller.ApiResponse;
import com.potato.api.controller.AbstractControllerTest;
import com.potato.api.controller.organization.api.OrganizationMockMvc;
import com.potato.api.service.organization.dto.request.RetrieveOrganizationsWithPaginationRequest;
import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberCreator;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.OrganizationCreator;
import com.potato.domain.domain.organization.OrganizationRepository;
import com.potato.domain.domain.organization.OrganizationRole;
import com.potato.common.exception.ErrorCode;
import com.potato.api.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.api.service.organization.dto.request.RetrievePopularOrganizationsRequest;
import com.potato.api.service.organization.dto.response.OrganizationInfoResponse;
import com.potato.api.service.organization.dto.response.OrganizationWithMembersInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.potato.api.helper.organization.OrganizationServiceTestUtils.assertMemberInOrganizationResponse;
import static com.potato.api.helper.organization.OrganizationServiceTestUtils.assertOrganizationInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;

class OrganizationControllerTest extends AbstractControllerTest {

    private OrganizationMockMvc organizationMockMvc;

    @Autowired
    private OrganizationRepository organizationRepository;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
        organizationMockMvc = new OrganizationMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationRepository.deleteAll();
    }

    @DisplayName("POST /api/v1/organization 200 OK")
    @Test
    void 새로운_그룹을_생성하면_그룹의_정보가_반환된다() throws Exception {
        // given
        String subDomain = "potato";
        String name = "감자";
        String description = "개발의 감을 잡자";
        String profileUrl = "https://profile.com";

        CreateOrganizationRequest request = CreateOrganizationRequest.testBuilder()
            .subDomain(subDomain)
            .name(name)
            .description(description)
            .profileUrl(profileUrl)
            .build();

        // when
        ApiResponse<OrganizationInfoResponse> response = organizationMockMvc.createOrganization(request, token, 200);

        // then
        assertOrganizationInfoResponse(response.getData(), subDomain, name, description, profileUrl, OrganizationCategory.NON_APPROVED_CIRCLE, 1);
    }

    @DisplayName("POST /api/v1/organization 401 ERROR")
    @Test
    void 로그인하지_않고_새로운_그룹을_요청시_UNAUTHORIZED_에러_가_발생한다() throws Exception {
        // given
        String subDomain = "potato";
        String name = "감자";
        String description = "개발의 감을 잡자";
        String profileUrl = "https://profile.com";

        CreateOrganizationRequest request = CreateOrganizationRequest.testBuilder()
            .subDomain(subDomain)
            .name(name)
            .description(description)
            .profileUrl(profileUrl)
            .build();

        // when
        ApiResponse<OrganizationInfoResponse> response = organizationMockMvc.createOrganization(request, "wrong Token", 401);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION.getCode());
    }

    @DisplayName("GET /api/v1/organization 200 OK")
    @Test
    void 그룹의_상세정보를_조회하면_그룹정보와_그룹에_속한_멤버들의_정보가_조회된다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        // when
        ApiResponse<OrganizationWithMembersInfoResponse> response = organizationMockMvc.getDetailOrganizationInfo(organization.getSubDomain(), token, 200);

        // then
        assertOrganizationInfoResponse(response.getData().getOrganization(), organization.getSubDomain(), organization.getName(), organization.getDescription(), organization.getProfileUrl(), organization.getCategory(), 1);

        assertThat(response.getData().getMembers()).hasSize(1);
        assertMemberInOrganizationResponse(response.getData().getMembers().get(0), testMember.getId(), testMember.getEmail(),
            testMember.getName(), testMember.getProfileUrl(), OrganizationRole.ADMIN);
    }

    @DisplayName("GET /api/v1/organization 200 OK")
    @Test
    void 그룹을_팔로우한_로그인한_유저가_그룹_상세_조회시_팔로우했다고_표시된다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        organization.addFollow(testMember.getId());
        organizationRepository.save(organization);

        // when
        ApiResponse<OrganizationWithMembersInfoResponse> response = organizationMockMvc.getDetailOrganizationInfo(organization.getSubDomain(), token, 200);

        // then
        System.out.println(response);
        assertThat(response.getData().getIsFollower()).isTrue();
    }

    @DisplayName("GET /api/v1/organization 200 OK")
    @Test
    void 특정_그룹_조회시_가입_승인_대기중인_경우_동아리_멤버에서_조회되지_않는다() throws Exception {
        // given
        Member pendingMember = memberRepository.save(MemberCreator.create("test@gmail.com"));

        organization.addAdmin(testMember.getId());
        organization.addPending(pendingMember.getId());
        organizationRepository.save(organization);

        // when
        ApiResponse<OrganizationWithMembersInfoResponse> response = organizationMockMvc.getDetailOrganizationInfo(organization.getSubDomain(), token, 200);

        // then
        assertThat(response.getData().getMembers()).hasSize(1);
        assertThat(response.getData().getMembers().get(0).getMemberId()).isEqualTo(testMember.getId());
    }

    @DisplayName("GET /api/v1/organization 404 NOT FOUND")
    @Test
    void 특정_그룹을_조회시_해당하는_그룹이_없는경우_404_에러가_발생한다() throws Exception {
        // when
        ApiResponse<OrganizationWithMembersInfoResponse> response = organizationMockMvc.getDetailOrganizationInfo(organization.getSubDomain(), token, 404);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION.getCode());
    }

    @DisplayName("GET /api/v1/organization 200 OK")
    @Test
    void 그룹을_팔로우하지_않은_로그인한_유저가_그룹_상세_조회를_하면_팔로우하지_않았다고_표시된다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        // when
        ApiResponse<OrganizationWithMembersInfoResponse> response = organizationMockMvc.getDetailOrganizationInfo(organization.getSubDomain(), token, 200);

        // then
        assertThat(response.getData().getIsFollower()).isFalse();
    }

    @DisplayName("GET /api/v1/organization 200 OK")
    @Test
    void 로그인_하지_않은_유저가_그룹_상세_조회를_하면_팔로우하지_않았다고_표시된다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        // when
        ApiResponse<OrganizationWithMembersInfoResponse> response = organizationMockMvc.getDetailOrganizationInfo(organization.getSubDomain(), "Wrong Token", 200);

        // then
        assertThat(response.getData().getIsFollower()).isFalse();
    }

    @DisplayName("GET /api/v1/organization 404 OK")
    @Test
    void 특정_그룹을_조회시_해당하는_그룹이_없는경우_404_NOT_FOUND() throws Exception {
        // when
        ApiResponse<OrganizationWithMembersInfoResponse> response = organizationMockMvc.getDetailOrganizationInfo(organization.getSubDomain(), token, 404);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION.getCode());
    }

    @DisplayName("GET /api/v1/organization/list 200 OK")
    @Test
    void 전체_등록된_그룹들을_페이지네이션으로_조회하면_그룹에_대한_간단한_정보들이_조회된다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        RetrieveOrganizationsWithPaginationRequest request = RetrieveOrganizationsWithPaginationRequest.testInstance(null, 0, 5);

        // when
        ApiResponse<List<OrganizationInfoResponse>> response = organizationMockMvc.retrieveOrganizationsWithPagination(request, 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertOrganizationInfoResponse(response.getData().get(0), organization.getSubDomain(), organization.getName(),
            organization.getDescription(), organization.getProfileUrl(), OrganizationCategory.NON_APPROVED_CIRCLE, 1);
    }

    @DisplayName("GET /api/v1/organization/list 200 OK")
    @Test
    void 모든_카테고리의_동아리_리스트를_조회한다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        Organization organization2 = OrganizationCreator.create("subDomain2");
        organization2.addAdmin(testMember.getId());
        organizationRepository.saveAll(Arrays.asList(organization, organization2));

        RetrieveOrganizationsWithPaginationRequest request = RetrieveOrganizationsWithPaginationRequest.testInstance(null, 0, 5);

        // when
        ApiResponse<List<OrganizationInfoResponse>> response = organizationMockMvc.retrieveOrganizationsWithPagination(request, 200);

        // then
        assertThat(response.getData()).hasSize(2);
        assertOrganizationInfoResponse(response.getData().get(0), organization2.getSubDomain());
        assertOrganizationInfoResponse(response.getData().get(1), organization.getSubDomain());
    }

    @DisplayName("GET /api/v1/organization/list SIZE만큼 최신순으로 조회한다 200 OK")
    @Test
    void lastOrganizationId를_0으로_요청하면_가장_최신에_개설된_동아리를_SIZE_수_만큼_조회한다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        Organization organization2 = OrganizationCreator.create("subDomain2");
        organization2.addAdmin(testMember.getId());
        organizationRepository.saveAll(Arrays.asList(organization, organization2));

        RetrieveOrganizationsWithPaginationRequest request = RetrieveOrganizationsWithPaginationRequest.testInstance(null, 0, 1);

        // when
        ApiResponse<List<OrganizationInfoResponse>> response = organizationMockMvc.retrieveOrganizationsWithPagination(request, 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertOrganizationInfoResponse(response.getData().get(0), organization2.getSubDomain());
    }

    @DisplayName("GET /api/v1/organization/list 스크롤 기반 페이지네이션 200 OK")
    @Test
    void lastOrganizationId이_0이_아니면_id_보다_이전에_개설된_동아리를_SIZE_수_만큼_조회한다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        Organization organization2 = OrganizationCreator.create("subDomain2");
        organization2.addAdmin(testMember.getId());
        organizationRepository.saveAll(Arrays.asList(organization, organization2));

        RetrieveOrganizationsWithPaginationRequest request = RetrieveOrganizationsWithPaginationRequest.testInstance(null, organization2.getId(), 1);

        // when
        ApiResponse<List<OrganizationInfoResponse>> response = organizationMockMvc.retrieveOrganizationsWithPagination(request, 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertOrganizationInfoResponse(response.getData().get(0), organization.getSubDomain());
    }

    @DisplayName("GET /api/v1/organization/list 특정 카테고리만 조회 200 OK")
    @Test
    void 인준_동아리만_조회하면_비인준_동아리는_포함되지_않는다() throws Exception {
        // given
        Organization nonApproved = OrganizationCreator.createNonApproved("nonapproved", "비인준 동아리");
        nonApproved.addAdmin(testMember.getId());
        Organization approved = OrganizationCreator.createApproved("approved", "인준 동아리");
        approved.addAdmin(testMember.getId());
        organizationRepository.saveAll(Arrays.asList(nonApproved, approved));

        RetrieveOrganizationsWithPaginationRequest request = RetrieveOrganizationsWithPaginationRequest.testInstance(OrganizationCategory.APPROVED_CIRCLE, 0, 5);

        // when
        ApiResponse<List<OrganizationInfoResponse>> response = organizationMockMvc.retrieveOrganizationsWithPagination(request, 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertOrganizationInfoResponse(response.getData().get(0), approved.getSubDomain());
    }

    @DisplayName("GET /api/v1/organization/list/popular 200 OK")
    @Test
    void 인기있는_그룹들을_조회하면_그룹들의_정보가_반환된다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        organization.addFollow(100L);

        Organization organization2 = OrganizationCreator.create("subDomain1");
        organization2.addAdmin(testMember.getId());

        organizationRepository.saveAll(Arrays.asList(organization, organization2));

        // when
        RetrievePopularOrganizationsRequest request = RetrievePopularOrganizationsRequest.testInstance(5);
        ApiResponse<List<OrganizationInfoResponse>> response = organizationMockMvc.getPopularOrganizations(request, 200);

        // then
        assertThat(response.getData()).hasSize(2);
        assertOrganizationInfoResponse(response.getData().get(0), organization.getSubDomain(), organization.getName(),
            organization.getDescription(), organization.getProfileUrl(), OrganizationCategory.NON_APPROVED_CIRCLE, 1);
        assertOrganizationInfoResponse(response.getData().get(1), organization2.getSubDomain(), organization2.getName(),
            organization2.getDescription(), organization2.getProfileUrl(), OrganizationCategory.NON_APPROVED_CIRCLE, 1);
    }

    @DisplayName("GET /api/v1/organization/my 200 OK")
    @Test
    void 내가_가입하거나_생성한_그룹들을_조회하면_그룹들의_간단한_정보가_조회된다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        // when
        ApiResponse<List<OrganizationInfoResponse>> response = organizationMockMvc.getMyOrganizations(token, 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertOrganizationInfoResponse(response.getData().get(0), organization.getSubDomain(), organization.getName(),
            organization.getDescription(), organization.getProfileUrl(), OrganizationCategory.NON_APPROVED_CIRCLE, 1);
    }

    @DisplayName("POST /api/v1/organization/join/apply 200 OK")
    @Test
    void 특정그룹에_가입신청을_하는_경우_200_OK() throws Exception {
        //given
        organization.addAdmin(100L);
        organizationRepository.save(organization);

        //when
        ApiResponse<String> response = organizationMockMvc.applyJoiningOrganization(organization.getSubDomain(), token, 200);

        //then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @DisplayName("POST /api/v1/organization/join/apply 409 CONFLICT")
    @Test
    void 특정그룹에_이미_가입신청을_했는데_가입신청하는_경우_409_CONFLICT() throws Exception {
        //given
        organization.addAdmin(100L);
        organization.addPending(testMember.getId());
        organizationRepository.save(organization);

        //when
        ApiResponse<String> response = organizationMockMvc.applyJoiningOrganization(organization.getSubDomain(), token, 409);

        //then
        assertThat(response.getCode()).isEqualTo(ErrorCode.CONFLICT_EXCEPTION.getCode());
    }

    @DisplayName("PUT /api/v1/organization/join/cancel 200 OK")
    @Test
    void 특정_그룹에_가입신청을_취소하는_경우_200_OK() throws Exception {
        // given
        organization.addAdmin(100L);
        organization.addPending(testMember.getId());
        organizationRepository.save(organization);

        // when
        ApiResponse<String> response = organizationMockMvc.cancelJoiningOrganization(organization.getSubDomain(), token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @DisplayName("PUT /api/v1/organization/join/cancel 404 NOT FOUND")
    @Test
    void 특정_그룹에_가입신청하지_않은_유저가_가입_취소하는_경우_404_OK() throws Exception {
        // when
        ApiResponse<String> response = organizationMockMvc.cancelJoiningOrganization(organization.getSubDomain(), token, 404);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION.getCode());
    }

    @DisplayName("DELETE /api/v1/organization/leave 200 OK")
    @Test
    void 특정_그룹에서_회장과_회원이_있을때_회원이_탈퇴하는_경우_200_OK() throws Exception {
        // given
        organization.addAdmin(100L);
        organization.addUser(testMember.getId());
        organizationRepository.save(organization);

        // when
        ApiResponse<String> response = organizationMockMvc.leaveFromOrganization(organization.getSubDomain(), token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @DisplayName("DELETE /api/v1/organization/leave 403 FORBIDDEN")
    @Test
    void 특정_그룹에서_회장이_탈퇴하는_경우_애러발생() throws Exception {
        //given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        //when
        ApiResponse<String> response = organizationMockMvc.leaveFromOrganization(organization.getSubDomain(), token, 403);

        //then
        assertThat(response.getCode()).isEqualTo(ErrorCode.FORBIDDEN_EXCEPTION.getCode());
    }

}
