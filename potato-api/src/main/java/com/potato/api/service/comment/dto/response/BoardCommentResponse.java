package com.potato.api.service.comment.dto.response;

import com.potato.api.service.common.dto.response.BaseTimeResponse;
import com.potato.api.service.member.dto.response.MemberInfoResponse;
import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.comment.BoardComment;
import com.potato.domain.domain.member.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCommentResponse extends BaseTimeResponse {

    private Long id;

    private BoardType type;

    private Long boardId;

    private String content;

    private int boardCommentLikeCounts;

    private Boolean isLike;

    private MemberInfoResponse author;

    private final List<BoardCommentResponse> children = new ArrayList<>();

    @Builder
    private BoardCommentResponse(Long id, BoardType type, Long boardId, MemberInfoResponse author,
                                 String content, int boardCommentLikeCounts, boolean isLike) {
        this.id = id;
        this.type = type;
        this.boardId = boardId;
        this.author = author;
        this.content = content;
        this.boardCommentLikeCounts = boardCommentLikeCounts;
        this.isLike = isLike;
    }

    public static BoardCommentResponse of(BoardComment comment, Long memberId, Map<Long, Member> memberMap) {
        BoardCommentResponse response = activeOrInActiveComment(comment, memberMap.get(comment.getMemberId()), memberId);
        List<BoardCommentResponse> childResponses = comment.getChildComments().stream()
            .map(boardComment -> BoardCommentResponse.of(boardComment, memberId, memberMap))
            .collect(Collectors.toList());
        response.children.addAll(childResponses);
        response.setBaseTime(comment);
        return response;
    }

    private static BoardCommentResponse activeOrInActiveComment(BoardComment comment, Member author, Long memberId) {
        if (comment.isDeleted()) {
            return inActiveComment(comment);
        }
        return activeComment(comment, author, memberId);
    }

    private static BoardCommentResponse inActiveComment(BoardComment comment) {
        return BoardCommentResponse.builder()
            .id(comment.getId())
            .type(comment.getType())
            .boardId(comment.getBoardId())
            .author(MemberInfoResponse.deletedMember())
            .content("삭제된 메시지입니다")
            .boardCommentLikeCounts(0)
            .isLike(false)
            .build();
    }

    private static BoardCommentResponse activeComment(BoardComment comment, Member author, Long memberId) {
        return BoardCommentResponse.builder()
            .id(comment.getId())
            .type(comment.getType())
            .boardId(comment.getBoardId())
            .author(MemberInfoResponse.of(author))
            .content(comment.getContent())
            .boardCommentLikeCounts(comment.getCommentLikeCounts())
            .isLike(comment.isLike(memberId))
            .build();
    }

}
