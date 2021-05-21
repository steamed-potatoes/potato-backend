package com.potato.api.service.image;

import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.image.BoardImage;
import com.potato.domain.domain.image.BoardImageCreator;
import com.potato.domain.domain.image.BoardImageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardImageServiceTest {

    @Autowired
    private BoardImageService boardImageService;

    @Autowired
    private BoardImageRepository boardImageRepository;

    @AfterEach
    void cleanUp() {
        boardImageRepository.deleteAll();
    }

    @Test
    void 새로운_게시물_이미지를_추가한다() {
        // given
        Long boardId = 100L;
        BoardType type = BoardType.ADMIN_BOARD;
        String imageUrl1 = "https://profile1.com";
        String imageUrl2 = "https://profile2.com";

        // when
        boardImageService.addImage(boardId, type, Arrays.asList(imageUrl1, imageUrl2));

        // then
        List<BoardImage> boardImageList = boardImageRepository.findAll();
        assertThat(boardImageList).hasSize(2);
        assertBoardImage(boardImageList.get(0), boardId, type, imageUrl1);
        assertBoardImage(boardImageList.get(1), boardId, type, imageUrl2);
    }

    @Test
    void 게시물_이미지를_수정한다() {
        // given
        Long boardId = 100L;
        BoardType type = BoardType.ADMIN_BOARD;
        String imageUrl1 = "https://profile1.com";
        String imageUrl2 = "https://profile2.com";

        boardImageRepository.save(BoardImageCreator.create(boardId, BoardType.ADMIN_BOARD, "https://test.com"));

        // when
        boardImageService.updateImage(boardId, type, Arrays.asList(imageUrl1, imageUrl2));

        // then
        List<BoardImage> boardImageList = boardImageRepository.findAll();
        assertThat(boardImageList).hasSize(2);
        assertBoardImage(boardImageList.get(0), boardId, type, imageUrl1);
        assertBoardImage(boardImageList.get(1), boardId, type, imageUrl2);
    }

    private void assertBoardImage(BoardImage boardImage, Long boardId, BoardType type, String imageUrl) {
        assertThat(boardImage.getBoardId()).isEqualTo(boardId);
        assertThat(boardImage.getType()).isEqualTo(type);
        assertThat(boardImage.getImageUrl()).isEqualTo(imageUrl);
    }

}
