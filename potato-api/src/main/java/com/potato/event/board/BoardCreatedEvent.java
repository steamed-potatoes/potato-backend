package com.potato.event.board;

import com.potato.domain.board.BoardType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreatedEvent {

    private final BoardType type;

    private final Long boardId;

    private final Long creatorId;

    private final List<String> hashTags;

    public static BoardCreatedEvent of(BoardType type, Long boardId, Long creatorId, List<String> hashTags) {
        return new BoardCreatedEvent(type, boardId, creatorId, hashTags);
    }

}
