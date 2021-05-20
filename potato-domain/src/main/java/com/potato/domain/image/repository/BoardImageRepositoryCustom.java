package com.potato.domain.image.repository;

import com.potato.domain.image.BoardImage;

import java.util.List;

public interface BoardImageRepositoryCustom {

    List<BoardImage> findBoardImageByOrganizationBoardId(Long boardId);

    BoardImage findMainImageByOrganizationBoardId(Long organizationBoardId);

}
