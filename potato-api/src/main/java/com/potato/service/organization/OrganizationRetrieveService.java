package com.potato.service.organization;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.organization.dto.response.OrganizationDetailInfoResponse;
import com.potato.service.organization.dto.response.OrganizationFollowMemberResponse;
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
    public OrganizationDetailInfoResponse getDetailOrganizationInfo(String subDomain) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        List<Member> memberList = memberRepository.findAllById(organization.getMemberIds());
        return OrganizationDetailInfoResponse.of(organization, memberList);
    }

    @Transactional(readOnly = true)
    public List<OrganizationInfoResponse> getMyOrganizationsInfo(Long memberId) {
        List<Organization> organizations = organizationRepository.findAllByMemberId(memberId);
        return organizations.stream()
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
    public List<OrganizationFollowMemberResponse> getOrganizationFollowMember(String subDomain) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        List<Member> followMemberList = memberRepository.findAllById(organization.getFollowIds());
        return followMemberList.stream()
            .map(OrganizationFollowMemberResponse::of)
            .collect(Collectors.toList());
    }

}
