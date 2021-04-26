package com.potato.service.board.organization;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.board.organization.dto.request.RetrieveImminentBoardsRequest;
import com.potato.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import com.potato.service.board.organization.dto.response.OrganizationBoardWithCreatorInfoResponse;
import com.potato.service.member.MemberServiceUtils;
import com.potato.service.organization.OrganizationServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrganizationBoardRetrieveService {

    private final OrganizationBoardRepository organizationBoardRepository;
    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public OrganizationBoardWithCreatorInfoResponse retrieveBoard(Long organizationBoardId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardById(organizationBoardRepository, organizationBoardId);
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, organizationBoard.getSubDomain());
        Member creator = MemberServiceUtils.findMemberById(memberRepository, organizationBoard.getMemberId());
        return OrganizationBoardWithCreatorInfoResponse.of(organizationBoard, organization, creator);
    }

    @Transactional(readOnly = true)
    public List<OrganizationBoardInfoResponse> retrieveBoardsWithPagination(long lastOrganizationBoardId, int size) {
        List<OrganizationBoard> findOrganizationBoards = OrganizationBoardServiceUtils.findOrganizationBoardWithPagination(organizationBoardRepository, lastOrganizationBoardId, size);
        return findOrganizationBoards.stream()
            .map(OrganizationBoardInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrganizationBoardInfoResponse> retrieveImminentBoards(RetrieveImminentBoardsRequest request) {
        return organizationBoardRepository.findBetweenDateLimit(request.getDateTime(), request.getDateTime().plusWeeks(1), request.getSize()).stream()
            .map(OrganizationBoardInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrganizationBoardInfoResponse> retrievePopularBoard() {
        return organizationBoardRepository.findPopularBoard().stream()
            .map(OrganizationBoardInfoResponse::of)
            .collect(Collectors.toList());
    }

}
