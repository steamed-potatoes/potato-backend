package com.potato.controller.hashtag;

import com.potato.event.board.BoardCreatedEvent;
import com.potato.event.board.BoardUpdatedEvent;
import com.potato.service.hashtag.BoardHashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HashTagEventListener {

    private final BoardHashTagService boardHashTagService;

    @EventListener
    public void addNewBoardHashTags(BoardCreatedEvent event) {
        boardHashTagService.addHashTag(event.getType(), event.getBoardId(), event.getCreatorId(), event.getHashTags());
    }

    @EventListener
    public void updateBoardHashTags(BoardUpdatedEvent event) {
        boardHashTagService.updateHashTags(event.getType(), event.getBoardId(), event.getCreatorId(), event.getHashTags());
    }

}
