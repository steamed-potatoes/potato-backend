package com.potato.service.board.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Getter
@NoArgsConstructor
public class RetrievePublicLatestBoardsRequest {

    @Min(0)
    private long lastBoardId;

    @Min(1)
    private int size;

}
