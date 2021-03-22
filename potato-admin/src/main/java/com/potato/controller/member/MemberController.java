package com.potato.controller.member;

import com.potato.config.interceptor.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.member.MemberService;
import com.potato.service.member.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @Auth
    @GetMapping("/admin/v1/member/list")
    public ApiResponse<List<MemberInfoResponse>> retrieveMembersInfo() {
        return ApiResponse.success(memberService.retrieveMembersInfo());
    }

}
