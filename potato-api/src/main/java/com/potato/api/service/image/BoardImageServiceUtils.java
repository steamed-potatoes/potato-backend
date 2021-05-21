package com.potato.api.service.image;

import com.potato.domain.domain.image.BoardImage;
import com.potato.domain.domain.image.BoardImageRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardImageServiceUtils {

    public static List<BoardImage> findBoardImages(BoardImageRepository boardImageRepository, Long boardId) {
        return boardImageRepository.findBoardImageByOrganizationBoardId(boardId);
    }

}
