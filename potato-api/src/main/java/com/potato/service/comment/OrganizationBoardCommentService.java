package com.potato.service.comment;

import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.service.board.organization.OrganizationBoardServiceUtils;
import com.potato.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrganizationBoardCommentService {

    private final OrganizationBoardRepository organizationBoardRepository;
    private final BoardCommentRepository boardCommentRepository;

    @Transactional
    public void addBoardComment(AddBoardCommentRequest request, Long memberId) {
        OrganizationBoardServiceUtils.validateExistsBoard(organizationBoardRepository, request.getBoardId());
        if (!request.hasParentComment()) {
            BoardComment boardComment = BoardCommentServiceUtils.findBoardCommentById(boardCommentRepository, request.getParentCommentId());
            boardComment.addChildComment(memberId, request.getContent());
            return;
        }
        boardCommentRepository.save(BoardComment.newRootComment(request.getType(), request.getBoardId(), memberId, request.getContent()));
    }

    @Transactional(readOnly = true)
    public List<BoardCommentResponse> retrieveBoardCommentList(Long organizationBoardId) {
        OrganizationBoardServiceUtils.validateExistsBoard(organizationBoardRepository, organizationBoardId);
        List<BoardComment> rootComments = boardCommentRepository.findRootCommentByOrganizationBoardId(organizationBoardId);
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
