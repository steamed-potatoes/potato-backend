package com.potato.domain.hashtag.repository;

import com.potato.domain.board.BoardType;
import com.potato.domain.hashtag.BoardHashTag;

import java.util.List;

public interface BoardHashTagRepositoryCustom {

    List<BoardHashTag> findAllByTypeAndBoardId(Long boardId, BoardType type);

}
