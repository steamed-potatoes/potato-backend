package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardRepository;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.board.dto.response.BoardDetailInfoResponse;
import com.potato.service.board.dto.response.BoardInfoResponse;
import com.potato.service.member.MemberServiceUtils;
import com.potato.service.organization.OrganizationServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<BoardInfoResponse> retrievePublicLatestBoardList(Long lastBoardId, int size) {
        return lastBoardId == 0 ? getLatestBoards(size) : getLatestBoardLessThanId(lastBoardId, size);
    }

    private List<BoardInfoResponse> getLatestBoards(int size) {
        return boardRepository.findPublicBoardsOrderByIdDesc(size).stream()
            .map(BoardInfoResponse::of)
            .collect(Collectors.toList());
    }

    private List<BoardInfoResponse> getLatestBoardLessThanId(Long lastBoardId, int size) {
        return boardRepository.findPublicBoardsLessThanOrderByIdDescLimit(lastBoardId, size).stream()
            .map(BoardInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardDetailInfoResponse getDetailBoard(Long boardId) {
        Board board = BoardServiceUtils.findPublicBoardById(boardRepository, boardId);
        Member member = MemberServiceUtils.findMemberById(memberRepository, board.getMemberId());
        return BoardDetailInfoResponse.of(board, member);
    }

    @Transactional
    public void addBoardLike(Long boardId, Long memberId) {
        Board board = BoardServiceUtils.findFetchBoardById(boardRepository, boardId);
        if (board.isPrivate()) {
            OrganizationServiceUtils.validateHasAuthority(organizationRepository, board.getSubDomain(), memberId);
        }
        board.addLike(memberId);
    }

    @Transactional
    public void cancelBoardLike(Long boardId, Long memberId) {
        Board board = BoardServiceUtils.findFetchBoardById(boardRepository, boardId);
        board.cancelLike(memberId);
    }

}
