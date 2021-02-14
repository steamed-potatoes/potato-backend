package com.potato.controller.member;

import com.potato.config.argumentResolver.LoginMember;
import com.potato.config.session.MemberSession;
import com.potato.controller.ApiResponse;
import com.potato.service.member.MemberService;
import com.potato.service.member.dto.request.CreateMemberRequest;
import com.potato.service.member.dto.response.MemberInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}) // Swagger 설정 (Bearer Token 입력)
    @GetMapping("/api/v1/member")
    public ApiResponse<MemberInfoResponse> getMyMemberInfo(@LoginMember MemberSession memberSession) {
        return ApiResponse.of(memberService.getMemberInfo(memberSession.getMemberId()));
    }

}
