package com.potato.domain.domain.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCommentCollection {

    private final List<BoardComment> boardComments = new ArrayList<>();

    private BoardCommentCollection(List<BoardComment> boardComments) {
        this.boardComments.addAll(boardComments);
    }

    public static BoardCommentCollection of(List<BoardComment> boardComments) {
        return new BoardCommentCollection(boardComments);
    }

    public List<Long> getAuthorIds() {
        return boardComments.stream()
            .map(BoardComment::getAuthorIdsInChildren)
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
    }

}
