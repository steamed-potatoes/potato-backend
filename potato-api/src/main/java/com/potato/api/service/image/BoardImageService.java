package com.potato.api.service.image;

import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.image.BoardImage;
import com.potato.domain.domain.image.BoardImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardImageService {

    private final BoardImageRepository boardImageRepository;

    @Transactional
    public void addImage(Long boardId, BoardType type, List<String> imageUrlList) {
        List<BoardImage> boardImageList = imageUrlList.stream()
            .map(imageUrl -> BoardImage.newInstance(boardId, type, imageUrl))
            .collect(Collectors.toList());
        boardImageRepository.saveAll(boardImageList);
    }

    @Transactional
    public void updateImage(Long boardId, BoardType type, List<String> imageUrlList) {
        List<BoardImage> boardImages = BoardImageServiceUtils.findBoardImages(boardImageRepository, boardId);
        boardImageRepository.deleteAll(boardImages);
        addImage(boardId, type, imageUrlList);
    }

}
