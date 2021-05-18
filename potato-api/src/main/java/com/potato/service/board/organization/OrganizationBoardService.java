package com.potato.service.board.organization;

import com.potato.domain.board.BoardType;
import com.potato.domain.board.organization.DeleteOrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.event.board.BoardCreatedEvent;
import com.potato.event.board.BoardUpdatedEvent;
import com.potato.service.board.organization.dto.request.CreateOrganizationBoardRequest;
import com.potato.service.board.organization.dto.request.LikeOrganizationBoardRequest;
import com.potato.service.board.organization.dto.request.UpdateOrganizationBoardRequest;
import com.potato.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrganizationBoardService {

    private final ApplicationEventPublisher eventPublisher;
    private final OrganizationBoardRepository organizationBoardRepository;
    private final DeleteOrganizationBoardRepository deleteOrganizationBoardRepository;

    @Transactional
    public OrganizationBoardInfoResponse createBoard(String subDomain, CreateOrganizationBoardRequest request, Long memberId) {
        OrganizationBoard organizationBoard = organizationBoardRepository.save(request.toEntity(subDomain, memberId));
        eventPublisher.publishEvent(BoardCreatedEvent.of(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), memberId, request.getHashTags(), request.getImageUrlList()));
        return OrganizationBoardInfoResponse.of(organizationBoard);
    }

    @Transactional
    public OrganizationBoardInfoResponse updateBoard(String subDomain, UpdateOrganizationBoardRequest request, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardByIdAndSubDomain(organizationBoardRepository, subDomain, request.getOrganizationBoardId());
        organizationBoard.updateInfo(request.getTitle(), request.getContent(), request.getStartDateTime(), request.getEndDateTime(), request.getType(), memberId);
        eventPublisher.publishEvent(BoardUpdatedEvent.of(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), memberId, request.getHashTags()));
        return OrganizationBoardInfoResponse.of(organizationBoard);
    }

    @Transactional
    public void likeOrganizationBoard(LikeOrganizationBoardRequest request, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardById(organizationBoardRepository, request.getOrganizationBoardId());
        organizationBoard.addLike(memberId);
    }

    @Transactional
    public void cancelLikeOrganizationBoard(LikeOrganizationBoardRequest request, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardById(organizationBoardRepository, request.getOrganizationBoardId());
        organizationBoard.cancelLike(memberId);
    }

    @Transactional
    public void deleteOrganizationBoard(String subDomain, Long organizationBoardId, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardByIdAndSubDomain(organizationBoardRepository, subDomain, organizationBoardId);
        deleteOrganizationBoardRepository.save(organizationBoard.delete(memberId));
        organizationBoardRepository.delete(organizationBoard);
    }

}
