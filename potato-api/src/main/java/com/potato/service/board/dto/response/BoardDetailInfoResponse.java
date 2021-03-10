package com.potato.service.board.dto.response;

import com.potato.domain.board.Board;
import com.potato.domain.member.Member;
import com.potato.service.member.dto.response.MemberInfoResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@ToString
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class BoardDetailInfoResponse {

    private final BoardInfoResponse board;

    private final MemberInfoResponse creator;

    public static BoardDetailInfoResponse of(Board board, Member member) {
        return new BoardDetailInfoResponse(BoardInfoResponse.of(board), MemberInfoResponse.of(member));
    }

}
