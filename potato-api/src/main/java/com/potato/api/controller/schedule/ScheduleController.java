package com.potato.api.controller.schedule;

import com.potato.api.controller.ApiResponse;
import com.potato.api.service.schedule.ScheduleService;
import com.potato.api.service.schedule.dto.request.ScheduleRequest;
import com.potato.api.service.schedule.dto.response.ScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "캘린더에 보여질 스케쥴 정보 조회")
    @GetMapping("/api/v1/schedule")
    public ApiResponse<ScheduleResponse> retrieveScheduleBetween(@Valid ScheduleRequest request) {
        return ApiResponse.success(scheduleService.getDefaultSchedule(request));
    }

}
