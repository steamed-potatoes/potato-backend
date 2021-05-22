package com.potato.api.service.board.organization;

import com.potato.domain.domain.board.organization.repository.dto.BoardWithOrganizationDto;
import com.potato.domain.domain.image.BoardImageRepository;
import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.domain.hashtag.BoardHashTag;
import com.potato.domain.domain.hashtag.BoardHashTagRepository;
import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberRepository;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationRepository;
import com.potato.api.service.board.organization.dto.request.RetrieveImminentBoardsRequest;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponseWithImage;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardWithCreatorInfoResponse;
import com.potato.api.service.hashtag.BoardHashTagServiceUtils;
import com.potato.api.service.member.MemberServiceUtils;
import com.potato.api.service.organization.OrganizationServiceUtils;
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
    private final BoardImageRepository boardImageRepository;

    @Transactional(readOnly = true)
    public OrganizationBoardWithCreatorInfoResponse retrieveBoardWithOrganizationAndCreator(Long organizationBoardId, Long memberId) {
        OrganizationBoard organizationBoard = OrganizationBoardServiceUtils.findOrganizationBoardById(organizationBoardRepository, organizationBoardId);
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, organizationBoard.getSubDomain());
        Member creator = MemberServiceUtils.findMemberById(memberRepository, organizationBoard.getMemberId());
        List<String> imageUrlList = OrganizationBoardServiceUtils.findOrganizationBoardImage(boardImageRepository, organizationBoard.getId());
        return OrganizationBoardWithCreatorInfoResponse.of(organizationBoard, organization, creator, getBoardHashTags(organizationBoard.getId()), imageUrlList, organizationBoard.hasLike(memberId));
    }

    private List<String> getBoardHashTags(Long boardId) {
        List<BoardHashTag> boardHashTags = BoardHashTagServiceUtils.findBoardHashTags(boardHashTagRepository, boardId, BoardType.ORGANIZATION_BOARD);
        return boardHashTags.stream()
            .map(BoardHashTag::getHashTag)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BoardWithOrganizationDto> retrieveBoardsWithPagination(OrganizationBoardCategory type, long lastOrganizationBoardId, int size) {
        return organizationBoardRepository.findAllWithOrganizationByTypeLessThanOrderByIdDescLimit(null, type, lastOrganizationBoardId, size).stream()
            .map(boardWithOrganizationDto -> boardWithOrganizationDto.setImageUrls(OrganizationBoardServiceUtils.findOrganizationBoardImage(boardImageRepository, boardWithOrganizationDto.getBoardId())))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrganizationBoardInfoResponseWithImage> retrieveImminentBoards(RetrieveImminentBoardsRequest request) {
        return organizationBoardRepository.findAllByBetweenDateTimeWithLimit(request.getDateTime(), request.getDateTime().plusWeeks(1), request.getSize()).stream()
            .map(organizationBoard -> OrganizationBoardInfoResponseWithImage.of(organizationBoard, OrganizationBoardServiceUtils.findOrganizationBoardImage(boardImageRepository, organizationBoard.getId())))
            .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<OrganizationBoardInfoResponse> retrievePopularBoard(int size) {
        return organizationBoardRepository.findAllOrderByLikesWithLimit(size).stream()
            .map(OrganizationBoardInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BoardWithOrganizationDto> getBoardsInOrganization(String subDomain, OrganizationBoardCategory type, long lastOrganizationBoardId, int size) {
        return organizationBoardRepository.findAllWithOrganizationByTypeLessThanOrderByIdDescLimit(subDomain, type, lastOrganizationBoardId, size).stream()
            .map(boardWithOrganizationDto -> boardWithOrganizationDto.setImageUrls(OrganizationBoardServiceUtils.findOrganizationBoardImage(boardImageRepository, boardWithOrganizationDto.getBoardId())))
            .collect(Collectors.toList());
    }

}
