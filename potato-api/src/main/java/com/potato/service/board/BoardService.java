package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.collection.BoardMemberCollection;
import com.potato.domain.board.BoardRepository;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.board.dto.response.BoardWithCreatorInfoResponse;
import com.potato.service.member.MemberServiceUtils;
import com.potato.service.organization.OrganizationServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<BoardWithCreatorInfoResponse> retrievePublicLatestBoardList(Long lastBoardId, int size) {
        return lastBoardId == 0 ? getLatestBoards(size) : getLatestBoardLessThanId(lastBoardId, size);
    }

    private List<BoardWithCreatorInfoResponse> getLatestBoards(int size) {
        List<Board> boardList = boardRepository.findPublicBoardsOrderByIdDesc(size);
        BoardMemberCollection collection = BoardMemberCollection.of(memberRepository, boardList);
        return boardList.stream()
            .map(board -> BoardWithCreatorInfoResponse.of(board, collection.getMember(board.getMemberId())))
            .collect(Collectors.toList());
    }

    private List<BoardWithCreatorInfoResponse> getLatestBoardLessThanId(Long lastBoardId, int size) {
        List<Board> boardList = boardRepository.findPublicBoardsLessThanOrderByIdDescLimit(lastBoardId, size);
        BoardMemberCollection collection = BoardMemberCollection.of(memberRepository, boardList);
        return boardList.stream()
            .map(board -> BoardWithCreatorInfoResponse.of(board, collection.getMember(board.getMemberId())))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardWithCreatorInfoResponse getDetailBoard(Long boardId) {
        Board board = BoardServiceUtils.findPublicBoardById(boardRepository, boardId);
        Member member = MemberServiceUtils.findMemberById(memberRepository, board.getMemberId());
        return BoardWithCreatorInfoResponse.of(board, member);
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
