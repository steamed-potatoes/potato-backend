package com.potato.service.comment;

import com.potato.domain.board.BoardRepository;
import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.board.BoardServiceUtils;
import com.potato.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public void addBoardComment(AddBoardCommentRequest request, Long memberId) {
        BoardServiceUtils.validateHasAuthority(boardRepository, organizationRepository, request.getBoardId(), memberId);
        if (!request.hasParentComment()) {
            BoardComment boardComment = BoardCommentServiceUtils.findBoardCommentById(boardCommentRepository, request.getParentCommentId());
            boardComment.addChildComment(memberId, request.getContent());
            return;
        }
        boardCommentRepository.save(BoardComment.newRootComment(request.getBoardId(), memberId, request.getContent()));
    }

    @Transactional(readOnly = true)
    public List<BoardCommentResponse> retrieveBoardCommentList(Long boardId, Long memberId) {
        BoardServiceUtils.validateHasAuthority(boardRepository, organizationRepository, boardId, memberId);
        List<BoardComment> rootComments = boardCommentRepository.findRootCommentByBoardId(boardId);
        return rootComments.stream()
            .map(BoardCommentResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteBoardComment(Long boardCommentId, Long memberId) {
        BoardComment comment = BoardCommentServiceUtils.findBoardCommentByIdAndMemberId(boardCommentRepository, boardCommentId, memberId);
        comment.delete();
    }

}
