package com.potato.api.controller.member;

import com.potato.api.controller.ApiResponse;
import com.potato.api.controller.ControllerTestUtils;
import com.potato.common.exception.ErrorCode;
import com.potato.domain.domain.member.MemberMajor;
import com.potato.api.service.member.dto.request.SignUpMemberRequest;
import com.potato.api.service.member.dto.request.UpdateMemberRequest;
import com.potato.api.service.member.dto.response.MajorInfoResponse;
import com.potato.api.service.member.dto.response.MemberInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest extends ControllerTestUtils {

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @Test
    void 회원가입_요청시_회원가입이_성공하면_SESSION_ID_가_반환된다() throws Exception {
        // given
        SignUpMemberRequest request = SignUpMemberRequest.testBuilder()
            .email("will.seungho@gmail.com")
            .name("강승호")
            .major(MemberMajor.IT_ICT)
            .classNumber(201610302)
            .profileUrl("https://profile.com")
            .build();

        // when
        ApiResponse<String> response = memberMockMvc.signUpMember(request, 200);

        // then
        assertThat(response.getData()).isNotNull();
    }

    @Test
    void 회원가입_요청시_이미_존재하는_이메일의경우_CONFLICT_에러() throws Exception {
        // given
        SignUpMemberRequest request = SignUpMemberRequest.testBuilder()
            .email(testMember.getEmail())
            .name("강승호")
            .major(MemberMajor.IT_ICT)
            .classNumber(201610302)
            .profileUrl("https://profile.com")
            .build();

        // when
        ApiResponse<String> response = memberMockMvc.signUpMember(request, 409);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.CONFLICT_EXCEPTION.getCode());
    }

    @Test
    void 회원가입_요청시_이메일을_입력하지_않으면_BAD_REQUEST_에러() throws Exception {
        // given
        SignUpMemberRequest request = SignUpMemberRequest.testBuilder()
            .name("강승호")
            .major(MemberMajor.IT_ICT)
            .classNumber(201610302)
            .profileUrl("https://profile.com")
            .build();

        // when
        ApiResponse<String> response = memberMockMvc.signUpMember(request, 400);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.VALIDATION_EXCEPTION.getCode());
    }

    @Test
    void 회원가입_요청시_이름을_입력하지_않으면_BAD_REQUEST_에러() throws Exception {
        // given
        SignUpMemberRequest request = SignUpMemberRequest.testBuilder()
            .email("will.seungho@gmail.com")
            .major(MemberMajor.IT_ICT)
            .classNumber(201610302)
            .profileUrl("https://profile.com")
            .build();

        // when
        ApiResponse<String> response = memberMockMvc.signUpMember(request, 400);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.VALIDATION_EXCEPTION.getCode());
    }

    @Test
    void SESSION_ID_를_이용해서_나의_정보를_조회한다() throws Exception {
        // when
        ApiResponse<MemberInfoResponse> response = memberMockMvc.getMyMemberInfo(token, 200);

        // then
        assertMemberInfoResponse(response.getData(), testMember.getEmail(), testMember.getName(), testMember.getMajorName(), testMember.getClassNumber(), testMember.getProfileUrl());
    }

    @Test
    void 토큰을_이용해_내_정보를_불러올때_잘못된_토큰이면_401_에러가_발생() throws Exception {
        // when
        ApiResponse<MemberInfoResponse> response = memberMockMvc.getMyMemberInfo("wrong token", 401);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION.getCode());
    }

    @Test
    void 내정보를_수정요청시_성공시_변경된_회원정보가_반환된다() throws Exception {
        // given
        String name = "승호강";
        String profileUrl = "https://abc.com";
        MemberMajor major = MemberMajor.IT_COMPUTER;
        int classNumber = 201710302;

        UpdateMemberRequest request = UpdateMemberRequest.testBuilder()
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .classNumber(classNumber)
            .build();

        // when
        ApiResponse<MemberInfoResponse> response = memberMockMvc.updateMemberInfo(request, token, 200);

        // then
        assertMemberInfoResponse(response.getData(), testMember.getEmail(), name, major.getName(), classNumber, profileUrl);
    }

    @Test
    void 특정_유저의_정보를_요청하면_회원의_정보가_조회된다() throws Exception {
        // when
        ApiResponse<MemberInfoResponse> response = memberMockMvc.getMemberInfo(testMember.getId(), 200);

        // then
        assertMemberInfoResponse(response.getData(), testMember.getEmail(), testMember.getName(), testMember.getMajorName(), testMember.getClassNumber(), testMember.getProfileUrl());
    }

    @Test
    void 존재하지_않는_유저를_조회하려하면_NOT_FOUND_EXCEPTION() throws Exception {
        // when
        ApiResponse<MemberInfoResponse> response = memberMockMvc.getMemberInfo(999L, 404);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION.getCode());
    }

    @Test
    void 등록된_전공_리스트를_조회한다() throws Exception {
        // when
        ApiResponse<List<MajorInfoResponse>> response = memberMockMvc.getMajors(200);

        // then
        assertThat(response.getData()).hasSize(MemberMajor.values().length);
    }

    private void assertMemberInfoResponse(MemberInfoResponse response, String email, String name, String major, int classNumber, String profileUrl) {
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getMajor()).isEqualTo(major);
        assertThat(response.getClassNumber()).isEqualTo(classNumber);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
    }

}
