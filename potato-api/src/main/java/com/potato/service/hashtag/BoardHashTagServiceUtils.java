package com.potato.service.hashtag;

import com.potato.domain.board.BoardType;
import com.potato.domain.hashtag.BoardHashTag;
import com.potato.domain.hashtag.BoardHashTagRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardHashTagServiceUtils {

    public static List<BoardHashTag> findBoardHashTags(BoardHashTagRepository boardHashTagRepository, Long boardId, BoardType type) {
        return boardHashTagRepository.findAllByTypeAndBoardId(boardId, type);
    }

}
