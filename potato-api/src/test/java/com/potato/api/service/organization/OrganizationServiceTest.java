package com.potato.api.service.organization;

import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberCreator;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.OrganizationCreator;
import com.potato.domain.domain.organization.OrganizationFollower;
import com.potato.domain.domain.organization.OrganizationFollowerRepository;
import com.potato.domain.domain.organization.OrganizationMemberMapper;
import com.potato.domain.domain.organization.OrganizationMemberMapperRepository;
import com.potato.domain.domain.organization.OrganizationRepository;
import com.potato.domain.domain.organization.OrganizationRole;
import com.potato.common.exception.model.ConflictException;
import com.potato.common.exception.model.ForbiddenException;
import com.potato.common.exception.model.NotFoundException;
import com.potato.api.service.MemberSetupTest;
import com.potato.api.service.organization.dto.request.CreateOrganizationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.potato.api.service.organization.OrganizationServiceTestUtils.*;
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
    private OrganizationFollowerRepository organizationFollowerRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationMemberMapperRepository.deleteAllInBatch();
        organizationFollowerRepository.deleteAllInBatch();
        organizationRepository.deleteAllInBatch();
    }

    private Long targetMemberId;
    private final String subDomain = "potato";

    @BeforeEach
    void setUpMember() {
        Member targetMember = memberRepository.save(MemberCreator.create("tnswh2023@naver.com"));
        targetMemberId = targetMember.getId();
    }

    @Test
    void 새로운_조직을_생성한다() {
        // given
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
            .subDomain(subDomain)
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
            .subDomain(subDomain)
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
    void 일반유저가_조직에_가입_신청을_한다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        //when
        organizationService.applyJoiningOrganization(subDomain, targetMemberId);

        //then
        List<OrganizationMemberMapper> organizationMemberMapperList = organizationMemberMapperRepository.findAll();
        assertThat(organizationMemberMapperList).hasSize(2);
        assertOrganizationMemberMapper(organizationMemberMapperList.get(0), memberId, OrganizationRole.ADMIN);
        assertOrganizationMemberMapper(organizationMemberMapperList.get(1), targetMemberId, OrganizationRole.PENDING);
    }

    @Test
    void 조직에_가입신청을_하는데_이미_가입중인_유저일_경우_에러가_발생한다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addUser(targetMemberId);
        organizationRepository.save(organization);

        // when & then
        assertThatThrownBy(
            () -> organizationService.applyJoiningOrganization(subDomain, targetMemberId)
        ).isInstanceOf(ConflictException.class);
    }

    @Test
    void 그룹에_가입_신청한_유저가_신청을_취소한다() {
        // given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addPending(targetMemberId);
        organizationRepository.save(organization);

        // when
        organizationService.cancelJoiningOrganization(subDomain, targetMemberId);

        // then
        List<OrganizationMemberMapper> organizationMemberMapperList = organizationMemberMapperRepository.findAll();
        assertThat(organizationMemberMapperList).isEmpty();
    }

    @Test
    void 이미_가입이_완료된경우_그룹_가입_신청을_취소할_수_없다() {
        // given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addUser(memberId);
        organizationRepository.save(organization);

        // when & then
        assertThatThrownBy(() -> organizationService.cancelJoiningOrganization(subDomain, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 그룹_가입_신청을_취소하는데_그룹_가입신청하지_않은경우_에러가_발생한다() {
        // given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        // when & then
        assertThatThrownBy(() -> organizationService.cancelJoiningOrganization(subDomain, 100L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 그룹의_일반_유저가_그룹에서_탈퇴한다() {
        // given
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
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        // when & then
        assertThatThrownBy(() -> organizationService.leaveFromOrganization(subDomain, memberId)).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 특정_그룹을_팔로우하면_정상적으로_처리된다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        //when
        organizationService.followOrganization(subDomain, memberId);

        //then
        List<OrganizationFollower> organizationFollowerList = organizationFollowerRepository.findAll();
        assertThat(organizationFollowerList).hasSize(1);
        assertThat(organizationFollowerList.get(0).getMemberId()).isEqualTo(memberId);
        assertThat(organizationFollowerList.get(0).getOrganization().getId()).isEqualTo(organization.getId());
    }

    @Test
    void 특정_그룹_팔로우를_이미_했는데_한번_더_팔로우하면_에러_발생() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addFollow(memberId);
        organizationRepository.save(organization);

        // when & then
        assertThatThrownBy(
            () -> organizationService.followOrganization(subDomain, memberId)
        ).isInstanceOf(ConflictException.class);
    }

    @Test
    void 없는_조직을_팔로우하면_에러가_발생한다() {
        //given
        //when & then
        assertThatThrownBy(
            () -> organizationService.followOrganization(subDomain, memberId)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 특정_그룹_팔로우를_취소한다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addFollow(memberId);
        organizationRepository.save(organization);

        //when
        organizationService.unFollowOrganization(subDomain, memberId);

        //then
        List<OrganizationFollower> followerList = organizationFollowerRepository.findAll();
        assertThat(followerList).isEmpty();

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList.get(0).getFollowersCount()).isEqualTo(0);
    }

    @Test
    void 팔로우_취소하려는데_팔로우가_안되어있는_유저일_경우() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        //when & then
        assertThatThrownBy(
            () -> organizationService.unFollowOrganization(subDomain, targetMemberId)
        ).isInstanceOf(NotFoundException.class).hasMessage(String.format("해당하는 조직(%s)에 팔로우한 멤버(%s)가 없습니다.", subDomain, targetMemberId));
    }

    @Test
    void 팔로우_취소하려는_조직이_존재하지_않을_경우() {
        //when & then
        assertThatThrownBy(
            () -> organizationService.unFollowOrganization(subDomain, memberId)
        ).isInstanceOf(NotFoundException.class);
    }

}
