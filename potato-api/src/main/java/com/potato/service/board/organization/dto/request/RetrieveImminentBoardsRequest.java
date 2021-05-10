package com.potato.service.board.organization.dto.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveImminentBoardsRequest {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    @Min(1)
    @NotNull
    private int size;

    public static RetrieveImminentBoardsRequest testInstance(LocalDateTime dateTime, int size) {
        return new RetrieveImminentBoardsRequest(dateTime, size);
    }

}
