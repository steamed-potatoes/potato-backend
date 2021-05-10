package com.potato.service.comment;

import com.potato.domain.board.BoardType;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.exception.model.NotFoundException;
import com.potato.service.board.admin.AdminBoardServiceUtils;
import com.potato.service.board.organization.OrganizationBoardServiceUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class BoardCommentServiceUtils {

    static BoardComment findBoardCommentById(BoardCommentRepository boardCommentRepository, Long boardCommentId) {
        BoardComment boardComment = boardCommentRepository.findBoardCommentByIdAndMemberId(boardCommentId, null);
        if (boardComment == null) {
            throw new NotFoundException(String.format("해당하는 id (%s)를 가진 댓글이 없습니다", boardCommentId));
        }
        return boardComment;
    }

    static BoardComment findBoardCommentByIdAndMemberId(BoardCommentRepository boardCommentRepository, Long boardCommentId, Long memberId) {
        BoardComment boardComment = boardCommentRepository.findBoardCommentByIdAndMemberId(boardCommentId, memberId);
        if (boardComment == null) {
            throw new NotFoundException(String.format("멤버 (%s)가 작성한 id(%s)를 가진 댓글이 없습니다", memberId, boardCommentId));
        }
        return boardComment;
    }

    static void validateExistBoard(OrganizationBoardRepository organizationBoardRepository, AdminBoardRepository adminBoardRepository, BoardType type, Long boardId) {
        if (type.equals(BoardType.ORGANIZATION_BOARD)) {
            OrganizationBoardServiceUtils.validateExistsBoard(organizationBoardRepository, boardId);
        } else if (type.equals(BoardType.ADMIN_BOARD)) {
            AdminBoardServiceUtils.validateExistBoard(adminBoardRepository, boardId);
        }
    }

}
