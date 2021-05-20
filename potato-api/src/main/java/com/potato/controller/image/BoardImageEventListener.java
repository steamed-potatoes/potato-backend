package com.potato.controller.image;

import com.potato.event.board.BoardCreatedEvent;
import com.potato.event.board.BoardUpdatedEvent;
import com.potato.service.image.BoardImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BoardImageEventListener {

    private final BoardImageService boardImageService;

    @EventListener
    public void addBoardImages(BoardCreatedEvent event) {
        boardImageService.addImage(event.getBoardId(), event.getImageUrlList(), event.getType());
    }

    @EventListener
    public void updateBoardImages(BoardUpdatedEvent event) {
        boardImageService.updateImage(event.getBoardId(), event.getImageUrlList(), event.getType());
    }

}
