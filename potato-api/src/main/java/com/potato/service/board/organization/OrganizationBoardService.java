package com.potato.service.board.organization;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.event.board.OrganizationBoardDeletedEvent;
import com.potato.service.board.organization.dto.request.CreateOrganizationBoardRequest;
import com.potato.service.board.organization.dto.request.LikeOrganizationBoardRequest;
import com.potato.service.board.organization.dto.request.UpdateOrganizationBoardRequest;
import com.potato.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import com.potato.service.board.organization.dto.response.OrganizationBoardWithCreatorInfoResponse;
import com.potato.service.member.MemberServiceUtils;
import com.potato.service.organization.OrganizationServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrganizationBoardService {

    private final ApplicationEventPublisher eventPublisher;
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

    @Transactional(readOnly = true)
    public List<OrganizationBoardInfoResponse> retrieveBoardsWithPagination(long lastOrganizationBoardId, int size) {
        List<OrganizationBoard> findOrganizationBoards = OrganizationBoardServiceUtils.findOrganizationBoardWithPagination(organizationBoardRepository, lastOrganizationBoardId, size);
        return findOrganizationBoards.stream()
            .map(OrganizationBoardInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrganizationBoardInfoResponse updateBoard(String subDomain, UpdateOrganizationBoardRequest request, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardByIdAndSubDomain(organizationBoardRepository, subDomain, request.getOrganizationBoardId());
        organizationBoard.updateInfo(request.getTitle(), request.getContent(), request.getImageUrl(), request.getStartDateTime(), request.getEndDateTime(), request.getType(), memberId);
        return OrganizationBoardInfoResponse.of(organizationBoard);
    }

    @Transactional
    public void likeOrganizationBoard(LikeOrganizationBoardRequest request, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardById(organizationBoardRepository, request.getOrganizationBoardId());
        if (request.getIsLike()) {
            organizationBoard.addLike(memberId);
            return;
        }
        organizationBoard.cancelLike(memberId);
    }

    @Transactional
    public void deleteOrganizationBoard(String subDomain, Long organizationBoardId, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardByIdAndSubDomain(organizationBoardRepository, subDomain, organizationBoardId);
        eventPublisher.publishEvent(OrganizationBoardDeletedEvent.of(organizationBoard, memberId));
        organizationBoardRepository.delete(organizationBoard);
    }

}
