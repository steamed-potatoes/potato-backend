package com.potato.api.service.comment;

import com.potato.api.service.comment.dto.request.DeleteBoardCommentRequest;
import com.potato.api.service.comment.dto.request.LikeBoardCommentRequest;
import com.potato.api.service.comment.dto.request.RetrieveBoardCommentsRequest;
import com.potato.domain.domain.board.admin.AdminBoardRepository;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.domain.comment.BoardComment;
import com.potato.domain.domain.comment.BoardCommentCollection;
import com.potato.domain.domain.comment.BoardCommentRepository;
import com.potato.api.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.api.service.comment.dto.request.UpdateBoardCommentRequest;
import com.potato.api.service.comment.dto.response.BoardCommentResponse;
import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardCommentService {

    private final AdminBoardRepository adminBoardRepository;
    private final OrganizationBoardRepository organizationBoardRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<BoardCommentResponse> retrieveBoardCommentList(RetrieveBoardCommentsRequest request, Long memberId) {
        BoardCommentServiceUtils.validateExistBoard(organizationBoardRepository, adminBoardRepository, request.getType(), request.getBoardId());
        List<BoardComment> rootComments = boardCommentRepository.findRootCommentByTypeAndBoardId(request.getType(), request.getBoardId());
        return rootComments.stream()
            .map(boardComment -> BoardCommentResponse.of(boardComment, memberId, getAuthorsInComments(rootComments)))
            .collect(Collectors.toList());
    }

    private Map<Long, Member> getAuthorsInComments(List<BoardComment> rootComments) {
        BoardCommentCollection collection = BoardCommentCollection.of(rootComments);
        return memberRepository.findAllByInIds(collection.getAuthorIds()).stream()
            .collect(Collectors.toMap(Member::getId, member -> member));
    }

    @Transactional
    public void addBoardComment(AddBoardCommentRequest request, Long memberId) {
        BoardCommentServiceUtils.validateExistBoard(organizationBoardRepository, adminBoardRepository, request.getType(), request.getBoardId());
        if (request.hasParentComment()) {
            BoardComment boardComment = BoardCommentServiceUtils.findBoardCommentById(boardCommentRepository, request.getParentCommentId());
            boardComment.addChildComment(memberId, request.getContent());
            return;
        }
        boardCommentRepository.save(BoardComment.newRootComment(request.getType(), request.getBoardId(), memberId, request.getContent()));
    }

    @Transactional
    public void updateBoardComment(UpdateBoardCommentRequest request, Long memberId) {
        BoardComment comment = BoardCommentServiceUtils.findBoardCommentByIdAndMemberId(boardCommentRepository, request.getBoardCommentId(), memberId);
        comment.update(request.getContent());
    }

    @Transactional
    public void deleteBoardComment(DeleteBoardCommentRequest request, Long memberId) {
        BoardComment comment = BoardCommentServiceUtils.findBoardCommentByIdAndMemberId(boardCommentRepository, request.getBoardCommentId(), memberId);
        comment.delete();
    }

    @Transactional
    public void likeBoardComment(LikeBoardCommentRequest request, Long memberId) {
        BoardComment boardComment = BoardCommentServiceUtils.findBoardCommentById(boardCommentRepository, request.getBoardCommentId());
        boardComment.addLike(memberId);
    }

    @Transactional
    public void cancelBoardCommentLike(LikeBoardCommentRequest request, Long memberId) {
        BoardComment boardComment = BoardCommentServiceUtils.findBoardCommentById(boardCommentRepository, request.getBoardCommentId());
        boardComment.cancelLike(memberId);
    }

}
