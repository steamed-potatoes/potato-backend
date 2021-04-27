package com.potato.service.organization;

import com.potato.domain.member.MemberRepository;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.member.dto.response.MemberInfoResponse;
import com.potato.service.organization.dto.response.OrganizationWithMembersInfoResponse;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrganizationRetrieveService {

    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public OrganizationWithMembersInfoResponse getDetailOrganizationInfo(String subDomain) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        return OrganizationWithMembersInfoResponse.of(organization, memberRepository.findAllById(organization.getMemberIds()));
    }

    @Transactional(readOnly = true)
    public List<OrganizationInfoResponse> getMyOrganizationsInfo(Long memberId) {
        return organizationRepository.findAllByMemberId(memberId).stream()
            .map(OrganizationInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrganizationInfoResponse> getOrganizationsInfo() {
        return organizationRepository.findAll().stream()
            .map(OrganizationInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrganizationInfoResponse> retrievePopularOrganizations(int size) {
        return organizationRepository.findOrganizationOrderByFollowersCountLimit(size).stream()
            .map(OrganizationInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getOrganizationFollowMember(String subDomain) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        return memberRepository.findAllById(organization.getFollowIds()).stream()
            .map(MemberInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrganizationInfoResponse> retrieveFollowingOrganizations(Long memberId) {
        List<Organization> organizationList = organizationRepository.findAllByFollowMemberId(memberId);
        return organizationList.stream()
            .map(OrganizationInfoResponse::of)
            .collect(Collectors.toList());
    }

}
