package com.potato.controller.schedule;

import com.potato.controller.ApiResponse;
import com.potato.service.schedule.ScheduleService;
import com.potato.service.schedule.dto.request.ScheduleRequest;
import com.potato.service.schedule.dto.response.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/api/v1/schedule")
    public ApiResponse<ScheduleResponse> retrieveScheduleBetween(@Valid ScheduleRequest request) {
        return ApiResponse.success(scheduleService.getDefaultSchedule(request));
    }

}
