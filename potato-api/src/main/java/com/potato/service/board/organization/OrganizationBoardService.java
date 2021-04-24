package com.potato.service.board.organization;

import com.potato.domain.board.organization.DeleteOrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.service.board.organization.dto.request.CreateOrganizationBoardRequest;
import com.potato.service.board.organization.dto.request.LikeOrganizationBoardRequest;
import com.potato.service.board.organization.dto.request.UpdateOrganizationBoardRequest;
import com.potato.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrganizationBoardService {

    private final OrganizationBoardRepository organizationBoardRepository;
    private final DeleteOrganizationBoardRepository deleteOrganizationBoardRepository;

    @Transactional
    public OrganizationBoardInfoResponse createBoard(String subDomain, CreateOrganizationBoardRequest request, Long memberId) {
        return OrganizationBoardInfoResponse.of(organizationBoardRepository.save(request.toEntity(subDomain, memberId)));
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
