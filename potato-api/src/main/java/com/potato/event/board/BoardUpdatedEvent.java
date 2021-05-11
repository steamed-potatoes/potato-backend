package com.potato.event.board;

import com.potato.domain.board.BoardType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardUpdatedEvent {

    private final BoardType type;

    private final Long boardId;

    private final Long creatorId;

    private final List<String> hashTags = new ArrayList<>();

    public BoardUpdatedEvent(BoardType type, Long boardId, Long creatorId, List<String> hashTags) {
        this.type = type;
        this.boardId = boardId;
        this.creatorId = creatorId;
        this.hashTags.addAll(hashTags);
    }

    public static BoardUpdatedEvent of(BoardType type, Long boardId, Long creatorId, List<String> hashTags) {
        return new BoardUpdatedEvent(type, boardId, creatorId, hashTags);
    }

}
