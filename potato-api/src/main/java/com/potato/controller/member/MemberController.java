package com.potato.controller.member;

import com.potato.config.interceptor.auth.Auth;
import com.potato.config.argumentResolver.MemberId;
import com.potato.config.session.MemberSession;
import com.potato.controller.ApiResponse;
import com.potato.service.member.MemberService;
import com.potato.service.member.dto.request.CreateMemberRequest;
import com.potato.service.member.dto.request.UpdateMemberRequest;
import com.potato.service.member.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.potato.config.session.SessionConstants.AUTH_SESSION;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final HttpSession httpSession;
    private final MemberService memberService;

    @PostMapping("/api/v1/member")
    public ApiResponse<String> createMember(@Valid @RequestBody CreateMemberRequest request) {
        Long memberId = memberService.createMember(request);
        httpSession.setAttribute(AUTH_SESSION, MemberSession.of(memberId));
        return ApiResponse.of(httpSession.getId());
    }

    @Auth
    @GetMapping("/api/v1/member")
    public ApiResponse<MemberInfoResponse> getMyMemberInfo(@MemberId Long memberId) {
        return ApiResponse.of(memberService.getMemberInfo(memberId));
    }

    @Auth
    @PutMapping("/api/v1/member")
    public ApiResponse<MemberInfoResponse> updateMemberInfo(
        @Valid @RequestBody UpdateMemberRequest request, @MemberId Long memberId) {
        return ApiResponse.of(memberService.updateMemberInfo(request, memberId));
    }

    @Auth
    @GetMapping("/api/v1/member/{targetId}")
    public ApiResponse<MemberInfoResponse> getMemberOne(@PathVariable Long targetId) {
        return ApiResponse.of(memberService.getMemberInfo(targetId));
    }

}
