package com.potato.api.service.organization;

import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberCreator;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.OrganizationCreator;
import com.potato.domain.domain.organization.OrganizationRepository;
import com.potato.common.exception.model.NotFoundException;
import com.potato.api.service.MemberSetupTest;
import com.potato.api.service.member.dto.response.MemberInfoResponse;
import com.potato.api.service.organization.dto.response.OrganizationWithMembersInfoResponse;
import com.potato.api.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.potato.api.helper.member.MemberTestHelper.assertMemberInfoResponse;
import static com.potato.api.helper.organization.OrganizationServiceTestUtils.assertOrganizationInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrganizationRetrieveServiceTest extends MemberSetupTest {

    @Autowired
    private OrganizationRetrieveService organizationRetrieveService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationRepository.deleteAll();
    }

    private final String subDomain = "potato";

    @Test
    void 특정_그룹_조회시_가입_승인_대기중인_경우_동아리_멤버에서_조회되지_않는다() {
        // given
        Member pendingMember = memberRepository.save(MemberCreator.create("test@gmail.com"));

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addPending(pendingMember.getId());
        organizationRepository.save(organization);

        // when
        OrganizationWithMembersInfoResponse response = organizationRetrieveService.getDetailOrganizationInfo(subDomain, memberId);

        // then
        assertThat(response.getMembers()).hasSize(1);
        assertThat(response.getMembers().get(0).getMemberId()).isEqualTo(memberId);
    }

    @Test
    void 특정_그룹을_조회시_해당하는_그룹이_없는경우() {
        // when & then
        assertThatThrownBy(
            () -> organizationRetrieveService.getDetailOrganizationInfo(subDomain, memberId)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 모든_카테고리의_동아리_리스트를_조회한다() {
        // given
        Organization organization1 = OrganizationCreator.create(subDomain);
        organization1.addAdmin(memberId);

        Organization organization2 = OrganizationCreator.create("subDomain2");
        organization2.addAdmin(memberId);

        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        // when
        List<OrganizationInfoResponse> responses = organizationRetrieveService.retrieveOrganizationsWithPagination(null, 0, 3);

        // then
        assertThat(responses).hasSize(2);
        assertOrganizationInfoResponse(responses.get(0), organization2.getSubDomain());
        assertOrganizationInfoResponse(responses.get(1), organization1.getSubDomain());
    }

    @Test
    void 비인준동아리_리스트를_조회한다() {
        // given
        Organization nonApprovedCircle = OrganizationCreator.create(subDomain, OrganizationCategory.NON_APPROVED_CIRCLE);
        nonApprovedCircle.addAdmin(memberId);

        Organization approvedCircle = OrganizationCreator.create("subDomain2", OrganizationCategory.APPROVED_CIRCLE);
        approvedCircle.addAdmin(memberId);

        organizationRepository.saveAll(Arrays.asList(nonApprovedCircle, approvedCircle));

        // when
        List<OrganizationInfoResponse> responses = organizationRetrieveService.retrieveOrganizationsWithPagination(OrganizationCategory.NON_APPROVED_CIRCLE, 0, 3);

        // then
        assertThat(responses).hasSize(1);
        assertOrganizationInfoResponse(responses.get(0), nonApprovedCircle.getSubDomain());
    }

    @Test
    void 전체_동아리_리스트_조회시_SIZE_로_넘어온_수만큼_최신순으로_조회한다() {
        // given
        Organization organization1 = OrganizationCreator.create(subDomain, OrganizationCategory.NON_APPROVED_CIRCLE);
        organization1.addAdmin(memberId);

        Organization organization2 = OrganizationCreator.create("subDomain2", OrganizationCategory.APPROVED_CIRCLE);
        organization2.addAdmin(memberId);

        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        // when
        List<OrganizationInfoResponse> responses = organizationRetrieveService.retrieveOrganizationsWithPagination(null, 0, 1);

        // then
        assertThat(responses).hasSize(1);
        assertOrganizationInfoResponse(responses.get(0), organization2.getSubDomain());
    }

    @Test
    void 조직_리스트를_불러온다_아무_조직도_없으면_NULL_이아닌_빈배열을_반환한다() {
        // when
        List<OrganizationInfoResponse> responses = organizationRetrieveService.retrieveOrganizationsWithPagination(null, 0, 3);

        // then
        assertThat(responses).isEmpty();
        assertThat(responses).isNotNull();
    }

    @Test
    void 내가_속한_조직의_리스트를_모두_불러온다() {
        // given
        Organization organization1 = OrganizationCreator.create(subDomain);
        organization1.addAdmin(memberId);

        String subDomain2 = "subDomain2";
        Organization organization2 = OrganizationCreator.create(subDomain2);
        organization2.addUser(memberId);

        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        // when
        List<OrganizationInfoResponse> organizationInfoResponses = organizationRetrieveService.getMyOrganizationsInfo(memberId);

        // then
        assertThat(organizationInfoResponses).hasSize(2);
        assertOrganizationInfoResponse(organizationInfoResponses.get(0), subDomain);
        assertOrganizationInfoResponse(organizationInfoResponses.get(1), subDomain2);
    }

    @Test
    void 내가_속한_동아리를_모두_불러올때_가입신청중인_동아리는_포함되지_않는다() {
        // given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addPending(memberId);

        organizationRepository.save(organization);

        // when
        List<OrganizationInfoResponse> organizationInfoResponses = organizationRetrieveService.getMyOrganizationsInfo(memberId);

        // then
        assertThat(organizationInfoResponses).isEmpty();
    }

    @Test
    void 동아리를_팔로우한_유저들_불러오기() {
        //given
        String followingEmail1 = "tnswh1@gmail.com";
        String followingEmail2 = "tnswh2@gmail.com";
        Member followingMember1 = memberRepository.save(MemberCreator.create(followingEmail1));
        Member followingMember2 = memberRepository.save(MemberCreator.create(followingEmail2));

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addFollow(followingMember1.getId());
        organization.addFollow(followingMember2.getId());
        organizationRepository.save(organization);

        //when
        List<MemberInfoResponse> responses = organizationRetrieveService.getOrganizationFollowMember(subDomain);

        //then
        assertMemberInfoResponse(responses.get(0), followingMember1.getId(), followingEmail1);
        assertMemberInfoResponse(responses.get(1), followingMember2.getId(), followingEmail2);
    }

    @Test
    void 그룹을_팔로우한_유저가_없을경우_빈배열을_반환한다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        //when
        List<MemberInfoResponse> responses = organizationRetrieveService.getOrganizationFollowMember(subDomain);

        //then
        assertThat(responses).isEmpty();
    }

    @Test
    void 해당_멤버가_팔로우한_그룹들을_가져온다() {
        //given
        Member member = MemberCreator.create("tnswh2023@naver.com");
        memberRepository.save(member);

        Organization organization1 = OrganizationCreator.create("potato1");
        Organization organization2 = OrganizationCreator.create("potato2");

        organization1.addFollow(member.getId());
        organization2.addFollow(member.getId());
        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        //when
        List<OrganizationInfoResponse> responses = organizationRetrieveService.retrieveFollowingOrganizations(member.getId());

        //then
        assertThat(responses.get(0).getSubDomain()).isEqualTo("potato1");
        assertThat(responses.get(1).getSubDomain()).isEqualTo("potato2");
    }

    @Test
    void 해당_멤버가_팔로우한_그룹이_없을_경우_빈배열을_반환한다() {
        //given
        Member member = MemberCreator.create("tnswh2023@naver.com");
        memberRepository.save(member);

        //when
        List<OrganizationInfoResponse> responses = organizationRetrieveService.retrieveFollowingOrganizations(member.getId());

        //then
        assertThat(responses).isEmpty();
    }

    @Test
    void 인기있는_그룹_조회시_팔로우가_많은_그룹수_순서로_조회한다() {
        // given
        Organization organization1 = OrganizationCreator.create("first");
        organization1.addFollow(1L);
        organization1.addFollow(2L);

        Organization organization2 = OrganizationCreator.create("second");
        organization2.addFollow(1L);

        Organization organization3 = OrganizationCreator.create("third");

        organizationRepository.saveAll(Arrays.asList(organization1, organization2, organization3));

        // when
        List<OrganizationInfoResponse> organizationInfoResponses = organizationRetrieveService.retrievePopularOrganizations(3);

        // then
        assertThat(organizationInfoResponses).hasSize(3);
        assertOrganizationInfoResponse(organizationInfoResponses.get(0), organization1.getSubDomain());
        assertOrganizationInfoResponse(organizationInfoResponses.get(1), organization2.getSubDomain());
        assertOrganizationInfoResponse(organizationInfoResponses.get(2), organization3.getSubDomain());
    }

    @Test
    void 인기있는_그룹_조회시_그룹의_팔로우_수가_같은_경우_최신순으로_조회된다() {
        // given
        Organization organization1 = OrganizationCreator.create("first");
        Organization organization2 = OrganizationCreator.create("second");

        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        // when
        List<OrganizationInfoResponse> organizationInfoResponses = organizationRetrieveService.retrievePopularOrganizations(2);

        // then
        assertThat(organizationInfoResponses).hasSize(2);
        assertOrganizationInfoResponse(organizationInfoResponses.get(0), organization2.getSubDomain());
        assertOrganizationInfoResponse(organizationInfoResponses.get(1), organization1.getSubDomain());
    }

}
