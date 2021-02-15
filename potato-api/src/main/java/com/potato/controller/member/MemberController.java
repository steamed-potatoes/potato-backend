package com.potato.controller.member;

import com.potato.config.argumentResolver.LoginMember;
import com.potato.config.session.MemberSession;
import com.potato.controller.ApiResponse;
import com.potato.service.member.MemberService;
import com.potato.service.member.dto.request.CreateMemberRequest;
import com.potato.service.member.dto.request.UpdateMemberRequest;
import com.potato.service.member.dto.response.MemberInfoResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

    @Parameter(hidden = true, name = "memberSession", in = ParameterIn.QUERY) // Swagger 설정 (memberSession 안나오게)
    @GetMapping("/api/v1/member")
    public ApiResponse<MemberInfoResponse> getMyMemberInfo(@LoginMember MemberSession memberSession) {
        return ApiResponse.of(memberService.getMemberInfo(memberSession.getMemberId()));
    }

    @Parameter(hidden = true, name = "memberSession", in = ParameterIn.QUERY)
    @PutMapping("/api/v1/member")
    public ApiResponse<MemberInfoResponse> updateMemberInfo(
        @Valid @RequestBody UpdateMemberRequest request, @LoginMember MemberSession memberSession) {
        return ApiResponse.of(memberService.updateMemberInfo(request, memberSession.getMemberId()));
    }

    @Parameter(hidden = true, name = "memberSession", in = ParameterIn.QUERY)
    @GetMapping("/api/v1/member/{targetId}")
    public ApiResponse<MemberInfoResponse> getMemberOne(
        @LoginMember MemberSession memberSession,
        @PathVariable Long targetId) {
        return ApiResponse.of(memberService.getMemberOne(targetId));
    }

}
