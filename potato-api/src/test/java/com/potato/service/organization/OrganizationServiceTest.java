package com.potato.service.organization;

import com.potato.domain.follow.Follow;
import com.potato.domain.organization.*;
import com.potato.exception.ConflictException;
import com.potato.exception.ForbiddenException;
import com.potato.exception.NotFoundException;
import com.potato.service.MemberSetupTest;
import com.potato.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.service.organization.dto.request.FollowRequest;
import com.potato.service.organization.dto.response.OrganizationDetailInfoResponse;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.potato.service.organization.OrganizationServiceTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrganizationServiceTest extends MemberSetupTest {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMemberMapperRepository organizationMemberMapperRepository;

    @Autowired
    private FollowRepository followRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationMemberMapperRepository.deleteAllInBatch();
        followRepository.deleteAllInBatch();
        organizationRepository.deleteAllInBatch();
    }

    @Test
    void 새로운_조직을_생성한다() {
        // given
        String subDomain = "potato";
        String name = "찐 감자";
        String description = "개발 동아리 입니다";
        String profileUrl = "http://image.com";

        CreateOrganizationRequest request = CreateOrganizationRequest.testBuilder()
            .subDomain(subDomain)
            .name(name)
            .description(description)
            .profileUrl(profileUrl)
            .build();

        // when
        organizationService.createOrganization(request, memberId);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertOrganization(organizationList.get(0), subDomain, name, description, profileUrl);
    }

    @Test
    void 조직_생성시_서브_도메인이_중복되면_에러가_발생한다() {
        // given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        CreateOrganizationRequest request = CreateOrganizationRequest.testBuilder()
            .subDomain(subDomain)
            .name("찐감자")
            .build();

        // when & then
        assertThatThrownBy(
            () -> organizationService.createOrganization(request, memberId)
        ).isInstanceOf(ConflictException.class);
    }

    @Test
    void 조직을_생성하면_기본적으로_비인준_동아리가_된다() {
        // given
        CreateOrganizationRequest request = CreateOrganizationRequest.testBuilder()
            .subDomain("potato")
            .name("찐 감자")
            .build();

        // when
        organizationService.createOrganization(request, memberId);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertThat(organizationList.get(0).getCategory()).isEqualTo(OrganizationCategory.NON_APPROVED_CIRCLE);
    }

    @Test
    void 새로운_조직을_생성하면_자동으로_관리자가_되고_회원수가_1이된다() {
        // given
        CreateOrganizationRequest request = CreateOrganizationRequest.testBuilder()
            .subDomain("potato")
            .name("찐 감자")
            .build();

        // when
        organizationService.createOrganization(request, memberId);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList.get(0).getMembersCount()).isEqualTo(1);

        List<OrganizationMemberMapper> organizationMemberMapperList = organizationMemberMapperRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertOrganizationMemberMapper(organizationMemberMapperList.get(0), memberId, OrganizationRole.ADMIN);
    }

    @Test
    void 서브도메인을_통해_특정_조직의_간단한_정보를_불러온다() {
        // given
        String subDomain = "potato";
        String name = "찐 감자";
        String description = "개발 동아리 입니다";
        String profileUrl = "http://image.com";
        OrganizationCategory category = OrganizationCategory.NON_APPROVED_CIRCLE;

        Organization organization = OrganizationCreator.create(subDomain, name, description, profileUrl, category);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        // when
        OrganizationDetailInfoResponse response = organizationService.getDetailOrganizationInfo(subDomain);

        // then
        assertOrganizationInfoResponse(response, organization.getId(), subDomain, name, description, profileUrl, category, organization.getMembersCount());
    }

    @Test
    void 특정_조직을_조회시_해당하는_서브도메인이_없는경우() {
        // when & then
        assertThatThrownBy(
            () -> organizationService.getDetailOrganizationInfo("empty")
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 조직_리스트를_불러온다() {
        // given
        Organization organization1 = OrganizationCreator.create("고예림");
        organization1.addAdmin(memberId);

        Organization organization2 = OrganizationCreator.create("강승호");
        organization2.addAdmin(memberId);

        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        // when
        List<OrganizationInfoResponse> responses = organizationService.getOrganizationsInfo();

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getSubDomain()).isEqualTo("고예림");
        assertThat(responses.get(1).getSubDomain()).isEqualTo("강승호");
    }

    @Test
    void 조직_리스트를_불러온다_아무_조직도_없다() {
        // when
        List<OrganizationInfoResponse> responses = organizationService.getOrganizationsInfo();

        // then
        assertThat(responses).isEmpty();
    }

    @Test
    void 내가_속한_조직의_리스트를_모두_불러온다() {
        // given
        String subDomain1 = "subDomain1";
        Organization organization1 = OrganizationCreator.create(subDomain1);
        organization1.addAdmin(memberId);

        String subDomain2 = "subDomain2";
        Organization organization2 = OrganizationCreator.create(subDomain2);
        organization2.addUser(memberId);

        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        // when
        List<OrganizationInfoResponse> organizationInfoResponses = organizationService.getMyOrganizationsInfo(memberId);

        // then
        assertThat(organizationInfoResponses).hasSize(2);
        assertThat(organizationInfoResponses.get(0).getSubDomain()).isEqualTo(subDomain1);
        assertThat(organizationInfoResponses.get(1).getSubDomain()).isEqualTo(subDomain2);
    }

    @Test
    void 내가_속한_조직의_리스트를_모두_불러올때_가입신청된_조직은_포함되지_않는다() {
        // given
        String subDomain = "subDomain1";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addPending(memberId);

        organizationRepository.save(organization);

        // when
        List<OrganizationInfoResponse> organizationInfoResponses = organizationService.getMyOrganizationsInfo(memberId);

        // then
        assertThat(organizationInfoResponses).isEmpty();
    }

    @Test
    void 일반유저가_조직에_가입_신청을_한다() {
        //given
        String subDomain = "potato";
        Long applyUserId = 10L;

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        //when
        organizationService.applyJoiningOrganization(subDomain, applyUserId);

        //then
        List<OrganizationMemberMapper> organizationMemberMapperList = organizationMemberMapperRepository.findAll();
        assertThat(organizationMemberMapperList).hasSize(2);
        assertOrganizationMemberMapper(organizationMemberMapperList.get(0), memberId, OrganizationRole.ADMIN);
        assertOrganizationMemberMapper(organizationMemberMapperList.get(1), applyUserId, OrganizationRole.PENDING);
    }

    @Test
    void 조직에_가입신청을_하는데_이미_가입중인_유저일_경우_에러가_발생한다() {
        //given
        String subDomain = "potato";
        Long applyUserId = 10L;

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addUser(applyUserId);
        organizationRepository.save(organization);

        // when & then
        assertThatThrownBy(
            () -> organizationService.applyJoiningOrganization(subDomain, applyUserId)
        ).isInstanceOf(ConflictException.class);
    }

    @Test
    void 그룹에_가입_신청한_유저가_신청을_취소한다() {
        // given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addPending(memberId);
        organizationRepository.save(organization);

        // when
        organizationService.cancelJoiningOrganization(subDomain, memberId);

        // then
        List<OrganizationMemberMapper> organizationMemberMapperList = organizationMemberMapperRepository.findAll();
        assertThat(organizationMemberMapperList).isEmpty();
    }

    @Test
    void 이미_가입이_완료된경우_그룹_가입_신청을_취소할_수_없다() {
        // given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addUser(memberId);
        organizationRepository.save(organization);

        // when & then
        assertThatThrownBy(() -> organizationService.cancelJoiningOrganization(subDomain, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 그룹_가입_신청을_취소하는데_그룹_가입신청하지_않은경우_에러가_발생한다() {
        // given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organizationRepository.save(organization);

        // when & then
        assertThatThrownBy(() -> organizationService.cancelJoiningOrganization(subDomain, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 그룹의_일반_유저가_그룹에서_탈퇴한다() {
        // given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addUser(memberId);
        organizationRepository.save(organization);

        // when
        organizationService.leaveFromOrganization(subDomain, memberId);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertThat(organizationList.get(0).getMembersCount()).isEqualTo(0);

        List<OrganizationMemberMapper> organizationMemberMapperList = organizationMemberMapperRepository.findAll();
        assertThat(organizationMemberMapperList).isEmpty();
    }

    @Test
    void 그룹의_관리자는_그룹에서_탈퇴할수_없다() {
        // given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        // when & then
        assertThatThrownBy(() -> organizationService.leaveFromOrganization(subDomain, memberId)).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 그룹을_팔로우한다() {
        //given
        String subDomain = "감자";

        FollowRequest request = FollowRequest.testBuilder()
            .subDomain(subDomain)
            .build();

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        //when
        organizationService.followOrganization(request, memberId);

        //then
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(1);
    }

}
