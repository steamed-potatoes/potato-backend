package com.potato.api.service.hashtag;

import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.hashtag.BoardHashTag;
import com.potato.domain.domain.hashtag.BoardHashTagRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardHashTagServiceUtils {

    public static List<BoardHashTag> findBoardHashTags(BoardHashTagRepository boardHashTagRepository, Long boardId, BoardType type) {
        return boardHashTagRepository.findAllByTypeAndBoardId(boardId, type);
    }

}
