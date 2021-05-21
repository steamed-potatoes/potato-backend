package com.potato.api.service.comment;

import com.potato.domain.domain.board.admin.AdminBoardRepository;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.domain.comment.BoardComment;
import com.potato.domain.domain.comment.BoardCommentRepository;
import com.potato.domain.domain.board.BoardType;
import com.potato.api.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.api.service.comment.dto.request.UpdateBoardCommentRequest;
import com.potato.api.service.comment.dto.response.BoardCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardCommentService {

    private final AdminBoardRepository adminBoardRepository;
    private final OrganizationBoardRepository organizationBoardRepository;
    private final BoardCommentRepository boardCommentRepository;

    @Transactional
    public void addBoardComment(AddBoardCommentRequest request, Long memberId) {
        BoardCommentServiceUtils.validateExistBoard(organizationBoardRepository, adminBoardRepository, request.getType(), request.getBoardId());
        if (!request.hasParentComment()) {
            BoardComment boardComment = BoardCommentServiceUtils.findBoardCommentById(boardCommentRepository, request.getParentCommentId());
            boardComment.addChildComment(memberId, request.getContent());
            return;
        }
        boardCommentRepository.save(BoardComment.newRootComment(request.getType(), request.getBoardId(), memberId, request.getContent()));
    }

    @Transactional(readOnly = true)
    public List<BoardCommentResponse> retrieveBoardCommentList(BoardType type, Long boardId) {
        BoardCommentServiceUtils.validateExistBoard(organizationBoardRepository, adminBoardRepository, type, boardId);
        List<BoardComment> rootComments = boardCommentRepository.findRootCommentByTypeAndBoardId(type, boardId);
        return rootComments.stream()
            .map(BoardCommentResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateBoardComment(UpdateBoardCommentRequest request, Long memberId) {
        BoardComment comment = BoardCommentServiceUtils.findBoardCommentByIdAndMemberId(boardCommentRepository, request.getBoardCommentId(), memberId);
        comment.update(request.getContent());
    }

    @Transactional
    public void deleteBoardComment(Long boardCommentId, Long memberId) {
        BoardComment comment = BoardCommentServiceUtils.findBoardCommentByIdAndMemberId(boardCommentRepository, boardCommentId, memberId);
        comment.delete();
    }

    @Transactional
    public void likeBoardComment(Long boardCommentId, Long memberId) {
        BoardComment boardComment = BoardCommentServiceUtils.findBoardCommentById(boardCommentRepository, boardCommentId);
        boardComment.addLike(memberId);
    }

    @Transactional
    public void unLikeBoardComment(Long boardCommentId, Long memberId) {
        BoardComment boardComment = BoardCommentServiceUtils.findBoardCommentById(boardCommentRepository, boardCommentId);
        boardComment.deleteLike(memberId);
    }

}
