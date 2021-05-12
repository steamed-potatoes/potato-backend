package com.potato.event.board;

import com.potato.domain.board.BoardType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardUpdatedEvent {

    private final BoardType type;

    private final Long boardId;

    private final Long creatorId;

    private final List<String> hashTags;

    public static BoardUpdatedEvent of(BoardType type, Long boardId, Long creatorId, List<String> hashTags) {
        return new BoardUpdatedEvent(type, boardId, creatorId, hashTags);
    }

}
