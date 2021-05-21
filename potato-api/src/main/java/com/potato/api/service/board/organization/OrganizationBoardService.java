package com.potato.api.service.board.organization;

import com.potato.api.service.board.organization.dto.request.DeleteOrganizationBoardRequest;
import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.board.organization.DeleteOrganizationBoardRepository;
import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import com.potato.api.event.board.BoardCreatedEvent;
import com.potato.api.event.board.BoardUpdatedEvent;
import com.potato.api.service.board.organization.dto.request.CreateOrganizationBoardRequest;
import com.potato.api.service.board.organization.dto.request.LikeOrganizationBoardRequest;
import com.potato.api.service.board.organization.dto.request.UpdateOrganizationBoardRequest;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponseWithImage;
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
    public OrganizationBoardInfoResponseWithImage createBoard(String subDomain, CreateOrganizationBoardRequest request, Long memberId) {
        OrganizationBoard organizationBoard = organizationBoardRepository.save(request.toEntity(subDomain, memberId));
        eventPublisher.publishEvent(BoardCreatedEvent.of(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), memberId, request.getHashTags(), request.getImageUrlList()));
        return OrganizationBoardInfoResponseWithImage.of(organizationBoard, request.getImageUrlList());
    }

    @Transactional
    public OrganizationBoardInfoResponseWithImage updateBoard(String subDomain, UpdateOrganizationBoardRequest request, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardByIdAndSubDomain(organizationBoardRepository, subDomain, request.getOrganizationBoardId());
        organizationBoard.updateInfo(request.getTitle(), request.getContent(), request.getStartDateTime(), request.getEndDateTime(), request.getType(), memberId);
        eventPublisher.publishEvent(BoardUpdatedEvent.of(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), memberId, request.getHashTags(), request.getImageUrlList()));
        return OrganizationBoardInfoResponseWithImage.of(organizationBoard, request.getImageUrlList());
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
    public void deleteOrganizationBoard(String subDomain, DeleteOrganizationBoardRequest request, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardByIdAndSubDomain(organizationBoardRepository, subDomain, request.getOrganizationBoardId());
        deleteOrganizationBoardRepository.save(organizationBoard.delete(memberId));
        organizationBoardRepository.delete(organizationBoard);
    }

}
