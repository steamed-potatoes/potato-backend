package com.potato.service.board;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.board.dto.request.CreateOrganizationBoardRequest;
import com.potato.service.board.dto.request.UpdateOrganizationBoardRequest;
import com.potato.service.board.dto.response.OrganizationBoardInfoResponse;
import com.potato.service.board.dto.response.OrganizationBoardWithCreatorInfoResponse;
import com.potato.service.member.MemberServiceUtils;
import com.potato.service.organization.OrganizationServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrganizationBoardService {

    private final OrganizationBoardRepository organizationBoardRepository;
    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;
    private final DeleteOrganizationBoardService deleteOrganizationBoardService;

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

    @Transactional
    public OrganizationBoardInfoResponse updateBoard(String subDomain, UpdateOrganizationBoardRequest request, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardByIdAndSubDomain(organizationBoardRepository, subDomain, request.getOrganizationBoardId());
        organizationBoard.updateInfo(request.getTitle(), request.getContent(), request.getImageUrl(), request.getStartDateTime(), request.getEndDateTime(), request.getType(), memberId);
        return OrganizationBoardInfoResponse.of(organizationBoard);
    }

    @Transactional
    public void likeOrganizationBoard(Long organizationBoardId, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardById(organizationBoardRepository, organizationBoardId);
        organizationBoard.addLike(memberId);
    }

    @Transactional
    public void cancelOrganizationBoardLike(Long organizationBoardId, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardById(organizationBoardRepository, organizationBoardId);
        organizationBoard.cancelLike(memberId);
    }

    @Transactional
    public void deleteOrganizationBoard(String subDomain, Long organizationBoardId, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardByIdAndSubDomain(organizationBoardRepository, subDomain, organizationBoardId);
        deleteOrganizationBoardService.backUpOrganizationBoard(organizationBoard, memberId);
        organizationBoardRepository.delete(organizationBoard);
    }

    @Transactional
    public List<OrganizationBoardInfoResponse> retrieveLatestOrganizationBoardList(long lastOrganizationBoardId, int size) {
        return lastOrganizationBoardId == 0 ? getLatestOrganizationBoards(size) : getLatestOrganizationBoardsLessThanId(lastOrganizationBoardId, size);
    }

    private List<OrganizationBoardInfoResponse> getLatestOrganizationBoards(int size) {
        return organizationBoardRepository.findBoardsOrderByDesc(size).stream()
            .map(OrganizationBoardInfoResponse::of).collect(Collectors.toList());
    }

    private List<OrganizationBoardInfoResponse> getLatestOrganizationBoardsLessThanId(long lastOrganizationBoardId, int size) {
        return organizationBoardRepository.findBoardsLessThanOrderByIdDescLimit(lastOrganizationBoardId, size)
            .stream()
            .map(OrganizationBoardInfoResponse::of)
            .collect(Collectors.toList());
    }

}
