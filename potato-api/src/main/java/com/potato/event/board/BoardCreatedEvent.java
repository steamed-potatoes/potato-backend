package com.potato.event.board;

import com.potato.domain.board.BoardType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardCreatedEvent {

    private final BoardType type;

    private final Long boardId;

    private final Long creatorId;

    private final List<String> hashTags = new ArrayList<>();

    private BoardCreatedEvent(BoardType type, Long boardId, Long creatorId, List<String> hashTags) {
        this.type = type;
        this.boardId = boardId;
        this.creatorId = creatorId;
        this.hashTags.addAll(hashTags);
    }

    public static BoardCreatedEvent of(BoardType type, Long boardId, Long creatorId, List<String> hashTags) {
        return new BoardCreatedEvent(type, boardId, creatorId, hashTags);
    }

}
