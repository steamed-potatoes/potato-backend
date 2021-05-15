package com.potato.controller.member;

import com.potato.config.interceptor.auth.Auth;
import com.potato.config.argumentResolver.MemberId;
import com.potato.config.session.MemberSession;
import com.potato.controller.ApiResponse;
import com.potato.domain.member.MemberMajor;
import com.potato.service.member.MemberService;
import com.potato.service.member.dto.request.SignUpMemberRequest;
import com.potato.service.member.dto.request.UpdateMemberRequest;
import com.potato.service.member.dto.response.MajorInfoResponse;
import com.potato.service.member.dto.response.MemberInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.potato.config.session.SessionConstants.AUTH_SESSION;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final HttpSession httpSession;
    private final MemberService memberService;

    @Operation(summary = "회원가입을 요청하는 API")
    @PostMapping("/api/v1/member")
    public ApiResponse<String> signUpMember(@Valid @RequestBody SignUpMemberRequest request) {
        Long memberId = memberService.signUpMember(request);
        httpSession.setAttribute(AUTH_SESSION, MemberSession.of(memberId));
        return ApiResponse.success(httpSession.getId());
    }

    @Operation(summary = "나의 회원 정보를 불러오는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @GetMapping("/api/v1/member")
    public ApiResponse<MemberInfoResponse> getMyMemberInfo(@MemberId Long memberId) {
        return ApiResponse.success(memberService.getMemberInfo(memberId));
    }

    @Operation(summary = "나의 회원 정보를 수정하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PutMapping("/api/v1/member")
    public ApiResponse<MemberInfoResponse> updateMemberInfo(@Valid @RequestBody UpdateMemberRequest request, @MemberId Long memberId) {
        return ApiResponse.success(memberService.updateMemberInfo(request, memberId));
    }

    @Operation(summary = "특정 멤버의 회원 정보를 불러오는 API")
    @GetMapping("/api/v1/member/{targetId}")
    public ApiResponse<MemberInfoResponse> getMemberInfo(@PathVariable Long targetId) {
        return ApiResponse.success(memberService.getMemberInfo(targetId));
    }

    @Operation(summary = "등록된 전공 리스트를 조회하는 API")
    @GetMapping("/api/v1/major/list")
    public List<MajorInfoResponse> getMajorList() {
        return Arrays.stream(MemberMajor.values())
            .map(MajorInfoResponse::of)
            .collect(Collectors.toList());
    }

}
