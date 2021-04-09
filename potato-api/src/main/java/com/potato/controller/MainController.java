package com.potato.controller;

import com.potato.domain.member.MemberMajor;
import com.potato.service.member.dto.response.MajorInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MainController {

    @Operation(summary = "Health Check")
    @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.success("Potato API Server OK");
    }

    @Operation(summary = "등록된 전공 리스트 조회")
    @GetMapping("/api/v1/major/list")
    public List<MajorInfoResponse> getMajorList() {
        return Arrays.stream(MemberMajor.values())
            .map(MajorInfoResponse::of)
            .collect(Collectors.toList());
    }

}
