package com.potato.service.organization;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberCreator;
import com.potato.domain.organization.*;
import com.potato.exception.model.ForbiddenException;
import com.potato.exception.model.NotFoundException;
import com.potato.service.MemberSetupTest;
import com.potato.service.organization.dto.request.ManageOrganizationMemberRequest;
import com.potato.service.organization.dto.request.UpdateOrganizationInfoRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.potato.service.organization.OrganizationServiceTestUtils.assertOrganization;
import static com.potato.service.organization.OrganizationServiceTestUtils.assertOrganizationMemberMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrganizationAdminServiceTest extends MemberSetupTest {

    @Autowired
    private OrganizationAdminService organizationAdminService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMemberMapperRepository organizationMemberMapperRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationRepository.deleteAll();
    }

    private final String subDomain = "potato";
    private Long targetMemberId;

    @BeforeEach
    void setUpMember() {
        Member targetMember = memberRepository.save(MemberCreator.create("target@gmail.com"));
        targetMemberId = targetMember.getId();
    }

    @Test
    void 조직의_정보를_수정한다() {
        // given
        String name = "감자팀";
        String description = "감자 화이팅";
        String profileUrl = "http://localhost.com";

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        UpdateOrganizationInfoRequest request = UpdateOrganizationInfoRequest.testBuilder()
            .name(name)
            .description(description)
            .profileUrl(profileUrl)
            .build();

        // when
        organizationAdminService.updateOrganizationInfo(subDomain, request);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertOrganization(organizationList.get(0), subDomain, name, description, profileUrl);
    }

    @Test
    void 조직_신청을_수락한다() {
        // given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addPending(targetMemberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.of(targetMemberId);

        // when
        organizationAdminService.approveOrganizationMember(subDomain, request);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertThat(organizationList.get(0).getMembersCount()).isEqualTo(2);

        List<OrganizationMemberMapper> organizationMemberMapperList = organizationMemberMapperRepository.findAll();
        assertThat(organizationMemberMapperList).hasSize(2);
        assertOrganizationMemberMapper(organizationMemberMapperList.get(0), memberId, OrganizationRole.ADMIN);
        assertOrganizationMemberMapper(organizationMemberMapperList.get(1), targetMemberId, OrganizationRole.USER);
    }

    @Test
    void 조직_신청_수락시_조직에_신청을_하지_않은_멤버일_경우_에러가_발생한다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.of(targetMemberId);

        // when & then
        assertThatThrownBy(
            () -> organizationAdminService.approveOrganizationMember(subDomain, request)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 조직_신청_수락시_이미_조직에_유저로_참여하고_있는경우_에러가_발생한다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addUser(targetMemberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.of(targetMemberId);

        // when & then
        assertThatThrownBy(
            () -> organizationAdminService.approveOrganizationMember(subDomain, request)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 조직_신청_수락시_이미_조직에_관리로_참여하고_있는경우_에러가_발생한다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.of(targetMemberId);

        // when & then
        assertThatThrownBy(
            () -> organizationAdminService.approveOrganizationMember(subDomain, request)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 조직_신청한_유저를_관리자가_거절한다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addPending(targetMemberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.of(targetMemberId);

        // when
        organizationAdminService.denyOrganizationMember(subDomain, request);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertThat(organizationList.get(0).getMembersCount()).isEqualTo(1);

        List<OrganizationMemberMapper> organizationMemberMapperList = organizationMemberMapperRepository.findAll();
        assertThat(organizationMemberMapperList).hasSize(1);
        assertOrganizationMemberMapper(organizationMemberMapperList.get(0), memberId, OrganizationRole.ADMIN);
    }

    @Test
    void 조직_신청_거절시_조직에_신청을_하지_않은_멤버일_경우_에러가_발생한다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.of(targetMemberId);

        // when & then
        assertThatThrownBy(
            () -> organizationAdminService.denyOrganizationMember(subDomain, request)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 그룹의_관리자가_일반_유저를_강퇴시킨다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addUser(targetMemberId);
        organizationRepository.save(organization);

        // when
        organizationAdminService.kickOffOrganizationUserByAdmin(subDomain, targetMemberId);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertThat(organizationList.get(0).getMembersCount()).isEqualTo(1);

        List<OrganizationMemberMapper> organizationMemberMapperList = organizationMemberMapperRepository.findAll();
        assertThat(organizationMemberMapperList).hasSize(1);
        assertOrganizationMemberMapper(organizationMemberMapperList.get(0), memberId, OrganizationRole.ADMIN);
    }

    @Test
    void 그룹의_관리자가_관리자를_강퇴시킬_수없다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addAdmin(targetMemberId);
        organizationRepository.save(organization);

        // when & then
        assertThatThrownBy(() -> organizationAdminService.kickOffOrganizationUserByAdmin(subDomain, targetMemberId)).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 그룹_관리자가_그룹멤버에게_관리자를_임명한다() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addUser(targetMemberId);
        organizationRepository.save(organization);

        //when
        organizationAdminService.appointOrganizationAdmin(subDomain, targetMemberId, memberId);

        //then
        List<OrganizationMemberMapper> organizationMemberMapperList = organizationMemberMapperRepository.findAll();
        assertOrganizationMemberMapper(organizationMemberMapperList.get(0), memberId, OrganizationRole.USER);
        assertOrganizationMemberMapper(organizationMemberMapperList.get(1), targetMemberId, OrganizationRole.ADMIN);
    }

    @Test
    void 그룹_관리자가_그룹에_속해있지_않은_사람에게_임명하려고_하면_애러발생() {
        //given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        //when & then
        assertThatThrownBy(
            () -> organizationAdminService.appointOrganizationAdmin(subDomain, targetMemberId, memberId)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 그룹_관리자가_자신한테_그룹_관리자를_넘겨주는경우_에러가_발생한다() {
        // given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        // when & then
        assertThatThrownBy(
            () -> organizationAdminService.appointOrganizationAdmin(subDomain, memberId, memberId)
        ).isInstanceOf(ForbiddenException.class);
    }

}
