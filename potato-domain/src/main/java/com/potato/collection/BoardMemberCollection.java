package com.potato.collection;

import com.potato.domain.board.Board;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BoardMemberCollection {

    private final Map<Long, Member> memberMap = new HashMap<>();

    private BoardMemberCollection(MemberRepository memberRepository, List<Board> boardList) {
        this.memberMap.putAll(findMembersInBoardList(memberRepository, boardList));
    }

    private Map<Long, Member> findMembersInBoardList(MemberRepository memberRepository, List<Board> boardList) {
        return memberRepository.findAllById(getMemberIdsFrom(boardList)).stream()
            .collect(Collectors.toMap(Member::getId, member -> member));
    }

    private Set<Long> getMemberIdsFrom(List<Board> boardList) {
        return boardList.stream()
            .map(Board::getMemberId)
            .collect(Collectors.toSet());
    }

    public static BoardMemberCollection of(MemberRepository memberRepository, List<Board> boardList) {
        return new BoardMemberCollection(memberRepository, boardList);
    }

    public Member getMember(Long memberId) {
        return memberMap.get(memberId);
    }

}
