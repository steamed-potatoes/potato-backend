package com.potato.service.board.organization;

import com.potato.domain.board.BoardType;
import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.board.organization.repository.dto.BoardWithOrganizationDto;
import com.potato.domain.hashtag.BoardHashTag;
import com.potato.domain.hashtag.BoardHashTagRepository;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.board.organization.dto.request.RetrieveImminentBoardsRequest;
import com.potato.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import com.potato.service.board.organization.dto.response.OrganizationBoardWithCreatorInfoResponse;
import com.potato.service.hashtag.BoardHashTagServiceUtils;
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
    private final BoardHashTagRepository boardHashTagRepository;

    @Transactional(readOnly = true)
    public OrganizationBoardWithCreatorInfoResponse retrieveBoardWithOrganizationAndCreator(Long organizationBoardId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardById(organizationBoardRepository, organizationBoardId);
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, organizationBoard.getSubDomain());
        Member creator = MemberServiceUtils.findMemberById(memberRepository, organizationBoard.getMemberId());
        return OrganizationBoardWithCreatorInfoResponse.of(organizationBoard, organization, creator, getBoardHashTags(organizationBoard.getId()));
    }

    private List<String> getBoardHashTags(Long boardId) {
        List<BoardHashTag> boardHashTags = BoardHashTagServiceUtils.findBoardHashTags(boardHashTagRepository, boardId, BoardType.ORGANIZATION_BOARD);
        return boardHashTags.stream()
            .map(BoardHashTag::getHashTag)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BoardWithOrganizationDto> retrieveBoardsWithPagination(OrganizationBoardCategory type, long lastOrganizationBoardId, int size) {
        return organizationBoardRepository.findAllWithOrganizationByTypeLessThanOrderByIdDescLimit(type, lastOrganizationBoardId, size);
    }

    @Transactional(readOnly = true)
    public List<OrganizationBoardInfoResponse> retrieveImminentBoards(RetrieveImminentBoardsRequest request) {
        return organizationBoardRepository.findAllByBetweenDateTimeWithLimit(request.getDateTime(), request.getDateTime().plusWeeks(1), request.getSize()).stream()
            .map(OrganizationBoardInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrganizationBoardInfoResponse> retrievePopularBoard(int size) {
        return organizationBoardRepository.findAllOrderByLikesWithLimit(size).stream()
            .map(OrganizationBoardInfoResponse::of)
            .collect(Collectors.toList());
    }

}
