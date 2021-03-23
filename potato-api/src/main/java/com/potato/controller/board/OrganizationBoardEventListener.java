package com.potato.controller.board;

import com.potato.event.board.OrganizationBoardDeletedEvent;
import com.potato.service.board.DeleteOrganizationBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrganizationBoardEventListener {

    private final DeleteOrganizationBoardService deleteOrganizationBoardService;

    @EventListener
    public void backUpDeleteOrganizationBoard(OrganizationBoardDeletedEvent event) {
        deleteOrganizationBoardService.backUpOrganizationBoard(event.getOrganizationBoard(), event.getMemberId());
    }

}
