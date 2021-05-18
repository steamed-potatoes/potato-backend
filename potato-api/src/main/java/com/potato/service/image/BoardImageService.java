package com.potato.service.image;

import com.potato.domain.board.BoardImage;
import com.potato.domain.board.BoardImageRepository;
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
    public void addImage(Long boardId, List<String> imageUrlList) {
        List<BoardImage> boardImageList = imageUrlList.stream()
            .map(imageUrl -> BoardImage.newInstance(boardId, imageUrl))
            .collect(Collectors.toList());
        boardImageRepository.saveAll(boardImageList);
    }

}
