package com.potato.api.event.board;

import com.potato.domain.domain.board.BoardType;
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

    private final List<String> imageUrlList;

    public static BoardCreatedEvent of(BoardType type, Long boardId, Long creatorId, List<String> hashTags, List<String> imageUrlList) {
        return new BoardCreatedEvent(type, boardId, creatorId, hashTags, imageUrlList);
    }

}
