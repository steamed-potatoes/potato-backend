package com.potato.service.organization;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberCreator;
import com.potato.domain.organization.*;
import com.potato.exception.NotFoundException;
import com.potato.service.MemberSetupTest;
import com.potato.service.member.dto.response.MemberInfoResponse;
import com.potato.service.organization.dto.response.OrganizationWithMembersInfoResponse;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.potato.service.member.MemberServiceTestUtils.assertMemberInfoResponse;
import static com.potato.service.organization.OrganizationServiceTestUtils.assertOrganizationInfoResponse;
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
    void 서브도메인을_통해_특정_조직의_자세한_정보를_불러온다() {
        // given
        String name = "찐 감자";
        String description = "개발 동아리 입니다";
        String profileUrl = "http://image.com";
        OrganizationCategory category = OrganizationCategory.NON_APPROVED_CIRCLE;

        Organization organization = OrganizationCreator.create(subDomain, name, description, profileUrl, category);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        // when
        OrganizationWithMembersInfoResponse response = organizationRetrieveService.getDetailOrganizationInfo(subDomain);

        // then
        assertOrganizationInfoResponse(response.getOrganization(), organization.getId(), subDomain, name, description, profileUrl, category, organization.getMembersCount());
        assertThat(response.getMembers()).hasSize(1);
    }

    @Test
    void 특정_그룹을_조회시_해당하는_그룹이_없는경우() {
        // when & then
        assertThatThrownBy(
            () -> organizationRetrieveService.getDetailOrganizationInfo(subDomain)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 조직_리스트를_불러온다() {
        // given
        Organization organization1 = OrganizationCreator.create(subDomain);
        organization1.addAdmin(memberId);

        Organization organization2 = OrganizationCreator.create("subDomain2");
        organization2.addAdmin(memberId);

        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        // when
        List<OrganizationInfoResponse> responses = organizationRetrieveService.getOrganizationsInfo();

        // then
        assertThat(responses).hasSize(2);
        assertOrganizationInfoResponse(responses.get(0), organization1.getSubDomain());
        assertOrganizationInfoResponse(responses.get(1), organization2.getSubDomain());
    }

    @Test
    void 조직_리스트를_불러온다_아무_조직도_없다() {
        // when
        List<OrganizationInfoResponse> responses = organizationRetrieveService.getOrganizationsInfo();

        // then
        assertThat(responses).isEmpty();
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
    void 내가_속한_조직의_리스트를_모두_불러올때_가입신청된_조직은_포함되지_않는다() {
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
    void 그룹을_팔로우한_유저들_불러오기() {
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
        assertMemberInfoResponse(responses.get(0), followingEmail1, followingMember1.getName(), followingMember1.getProfileUrl(), followingMember1.getMajor(), followingMember1.getClassNumber());
        assertMemberInfoResponse(responses.get(1), followingEmail2, followingMember2.getName(), followingMember2.getProfileUrl(), followingMember2.getMajor(), followingMember2.getClassNumber());
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
    void 멤버가_팔로우한_조직들을_가져온다() {
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
    void 멤버가_팔로우한_조직이_없을_경우_빈배열을_반환한다() {
        //given
        Member member = MemberCreator.create("tnswh2023@naver.com");
        memberRepository.save(member);

        //when
        List<OrganizationInfoResponse> responses = organizationRetrieveService.retrieveFollowingOrganizations(member.getId());

        //then
        assertThat(responses).isEmpty();
    }

    @Test
    void 팔로우가_많은_그룹수부터_조회한다() {
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
    void 그룹의_팔로우_수가_같은_경우_최신순으로_조회된다() {
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
