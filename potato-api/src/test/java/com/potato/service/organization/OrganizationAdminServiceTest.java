package com.potato.service.organization;

import com.potato.domain.organization.*;
import com.potato.exception.ForbiddenException;
import com.potato.exception.NotFoundException;
import com.potato.service.MemberSetupTest;
import com.potato.service.organization.dto.request.ManageOrganizationMemberRequest;
import com.potato.service.organization.dto.request.UpdateOrganizationInfoRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.potato.service.organization.OrganizationServiceTestUtils.assertOrganization;
import static com.potato.service.organization.OrganizationServiceTestUtils.assertOrganizationMemberMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrganizationAdminServiceTest extends MemberSetupTest {

    @Autowired
    private OrganizationAdminService organizationAdminService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMemberMapperRepository organizationMemberMapperRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationMemberMapperRepository.deleteAllInBatch();
        organizationRepository.deleteAllInBatch();
    }

    @Test
    void 조직의_정보를_수정다() {
        // given
        String subDomain = "potato";
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
        organizationAdminService.updateOrganizationInfo(subDomain, request, memberId);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertOrganization(organizationList.get(0), subDomain, name, description, profileUrl);
    }

    @DisplayName("조직의 관리자가 아닌 유저는 조직의 정보를 수정할 수 없다")
    @Test
    void 조직의_관리자가_아니면_조직의_정보를_수정할_수_없다_1() {
        // given
        String subDomain = "potato";

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addUser(memberId);
        organizationRepository.save(organization);

        UpdateOrganizationInfoRequest request = UpdateOrganizationInfoRequest.testBuilder()
            .name("감자")
            .build();

        // when & then
        assertThatThrownBy(() -> {
            organizationAdminService.updateOrganizationInfo(subDomain, request, memberId);
        }).isInstanceOf(ForbiddenException.class);
    }

    @DisplayName("조직원이 아닌경우 조직의 정보를 수정할 수 없다")
    @Test
    void 조직의_관리자가_아니면_조직의_정보를_수정할_수_없다_2() {
        // given
        String subDomain = "potato";

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addUser(memberId);
        organizationRepository.save(organization);

        UpdateOrganizationInfoRequest request = UpdateOrganizationInfoRequest.testBuilder()
            .name("감자")
            .build();

        // when & then
        assertThatThrownBy(() -> {
            organizationAdminService.updateOrganizationInfo(subDomain, request, memberId);
        }).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 조직_신청을_수락한다() {
        // given
        String subDomain = "potato";
        Long targetMemberId = 20L;

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addPending(targetMemberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.testBuilder()
            .targetMemberId(targetMemberId)
            .build();

        // when
        organizationAdminService.approveOrganizationMember(subDomain, request, memberId);

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
        String subDomain = "potato";
        Long targetMemberId = 20L;

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.testBuilder()
            .targetMemberId(targetMemberId)
            .build();

        // when & then
        assertThatThrownBy(() -> {
            organizationAdminService.approveOrganizationMember(subDomain, request, memberId);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 조직_신청_수락시_이미_조직에_유저로_참여하고_있는경우_에러가_발생한다() {
        //given
        String subDomain = "potato";
        Long targetMemberId = 20L;

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addUser(targetMemberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.testBuilder()
            .targetMemberId(targetMemberId)
            .build();

        // when & then
        assertThatThrownBy(() -> {
            organizationAdminService.approveOrganizationMember(subDomain, request, memberId);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 조직_신청_수락시_이미_조직에_관리로_참여하고_있는경우_에러가_발생한다() {
        //given
        String subDomain = "potato";

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.testBuilder()
            .targetMemberId(memberId)
            .build();

        // when & then
        assertThatThrownBy(() -> {
            organizationAdminService.approveOrganizationMember(subDomain, request, memberId);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 조직_신청한_유저를_관리자가_거절한다() {
        //given
        String subDomain = "potato";
        Long targetMemberId = 20L;

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organization.addPending(targetMemberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.testBuilder()
            .targetMemberId(targetMemberId)
            .build();

        // when
        organizationAdminService.denyOrganizationMember(subDomain, request, memberId);

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
        String subDomain = "potato";
        Long targetMemberId = 20L;

        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        ManageOrganizationMemberRequest request = ManageOrganizationMemberRequest.testBuilder()
            .targetMemberId(targetMemberId)
            .build();

        // when & then
        assertThatThrownBy(() -> {
            organizationAdminService.denyOrganizationMember(subDomain, request, memberId);
        }).isInstanceOf(NotFoundException.class);
    }

}
