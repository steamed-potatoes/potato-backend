package com.potato.service.boardv2;

import com.potato.domain.boardV2.organization.OrganizationBoard;
import com.potato.domain.boardV2.organization.OrganizationBoardRepository;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.boardv2.dto.request.CreateOrganizationBoardRequest;
import com.potato.service.boardv2.dto.response.OrganizationBoardInfoResponse;
import com.potato.service.boardv2.dto.response.OrganizationBoardWithCreatorInfoResponse;
import com.potato.service.member.MemberServiceUtils;
import com.potato.service.organization.OrganizationServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrganizationBoardService {

    private final OrganizationBoardRepository organizationBoardRepository;
    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public OrganizationBoardInfoResponse createBoard(String subDomain, CreateOrganizationBoardRequest request, Long memberId) {
        return OrganizationBoardInfoResponse.of(organizationBoardRepository.save(request.toEntity(subDomain, memberId)));
    }

    @Transactional(readOnly = true)
    public OrganizationBoardWithCreatorInfoResponse retrieveBoard(Long organizationBoardId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardById(organizationBoardRepository, organizationBoardId);
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, organizationBoard.getSubDomain());
        Member creator = MemberServiceUtils.findMemberById(memberRepository, organizationBoard.getMemberId());
        return OrganizationBoardWithCreatorInfoResponse.of(organizationBoard, organization, creator);
    }

}
