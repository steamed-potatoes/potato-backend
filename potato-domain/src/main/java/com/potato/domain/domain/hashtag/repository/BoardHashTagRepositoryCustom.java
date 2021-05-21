package com.potato.domain.domain.hashtag.repository;

import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.hashtag.BoardHashTag;

import java.util.List;

public interface BoardHashTagRepositoryCustom {

    List<BoardHashTag> findAllByTypeAndBoardId(Long boardId, BoardType type);

}
