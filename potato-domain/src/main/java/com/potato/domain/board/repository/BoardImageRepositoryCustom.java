package com.potato.domain.board.repository;

import com.potato.domain.board.BoardImage;

import java.util.List;

public interface BoardImageRepositoryCustom {

    List<BoardImage> findBoardImageByOrganizationBoardId(Long id);

}
