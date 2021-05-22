package com.potato.admin.controller.member;

import com.potato.admin.service.member.dto.response.MemberInfoResponse;
import com.potato.admin.config.interceptor.Auth;
import com.potato.admin.controller.ApiResponse;
import com.potato.admin.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "가입되어 있는 멤버들 불러오기", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @GetMapping("/admin/v1/member/list")
    public ApiResponse<List<MemberInfoResponse>> retrieveMembersInfo() {
        return ApiResponse.success(memberService.retrieveMembersInfo());
    }

}
