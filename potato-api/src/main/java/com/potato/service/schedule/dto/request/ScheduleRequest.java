package com.potato.service.schedule.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor
public class ScheduleRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private ScheduleRequest(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static ScheduleRequest testInstance(LocalDate startDate, LocalDate endDate) {
        return new ScheduleRequest(startDate, endDate);
    }

}
