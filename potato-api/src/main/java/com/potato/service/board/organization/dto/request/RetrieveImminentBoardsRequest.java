package com.potato.service.board.organization.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveImminentBoardsRequest {

    @NotNull
    private LocalDateTime dateTime;

    @Min(1)
    @NotNull
    private int size;

    public static RetrieveImminentBoardsRequest testInstance(LocalDateTime dateTime, int size) {
        return new RetrieveImminentBoardsRequest(dateTime, size);
    }

}
