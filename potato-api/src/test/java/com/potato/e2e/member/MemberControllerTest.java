package com.potato.e2e.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.controller.ApiResponse;
import com.potato.controller.advice.ErrorCode;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberCreator;
import com.potato.domain.member.MemberMajor;
import com.potato.domain.member.MemberRepository;
import com.potato.e2e.api.MemberTestController;
import com.potato.service.member.dto.request.CreateMemberRequest;
import com.potato.service.member.dto.request.UpdateMemberRequest;
import com.potato.service.member.dto.response.MemberInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MemberTestController memberTestController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        memberTestController = new MemberTestController(mockMvc, objectMapper);
    }

    @Test
    void 회원가입_API_호출시_생성된_토큰이_반환된다() throws Exception {
        // given
        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .email("test.seungho@gmail.com")
            .name("강승호")
            .major(MemberMajor.IT_ICT)
            .classNumber(201610302)
            .profileUrl("http://profile.com")
            .build();

        // when
        ApiResponse<String> response = memberTestController.createMember(request);

        // then
        assertThat(response.getData()).isNotNull();
    }

    @Test
    void 회원가입_API_호출시_이미_존재하는_이메일의경우_409_에러() throws Exception {
        // given
        memberRepository.save(MemberCreator.create("test.seungho@gmail.com"));

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .email("test.seungho@gmail.com")
            .name("강승호")
            .major(MemberMajor.IT_ICT)
            .classNumber(201610302)
            .profileUrl("http://profile.com")
            .build();

        // when
        ApiResponse<String> response = memberTestController.createMember(request);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.CONFLICT_EXCEPTION.getCode());
    }

    @Test
    void 회원가입_API_호출시_이메일을_입력하지_않으면_400_에러() throws Exception {
        // given
        memberRepository.save(MemberCreator.create("will.seungho@gmail.com"));

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .name("강승호")
            .major(MemberMajor.IT_ICT)
            .classNumber(201610302)
            .profileUrl("http://profile.com")
            .build();

        // when
        ApiResponse<String> response = memberTestController.createMember(request);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.VALIDATION_EXCEPTION.getCode());
    }

    @Test
    void 회원가입_API_호출시_이름을_입력하지_않으면_400_에러() throws Exception {
        // given
        memberRepository.save(MemberCreator.create("will.seungho@gmail.com"));

        CreateMemberRequest request = CreateMemberRequest.testBuilder()
            .email("will.seungho@gmail.com")
            .major(MemberMajor.IT_ICT)
            .classNumber(201610302)
            .profileUrl("http://profile.com")
            .build();

        // when
        ApiResponse<String> response = memberTestController.createMember(request);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.VALIDATION_EXCEPTION.getCode());
    }

    @Test
    void 토큰을_이용해_내_정보를_불러온다() throws Exception {
        // given
        String token = memberTestController.getMockMemberToken();

        // when
        ApiResponse<MemberInfoResponse> response = memberTestController.getMyMemberInfo(token);

        // then
        assertThat(response.getData().getEmail()).isEqualTo("will.seungho@gmail.com");
        assertThat(response.getData().getName()).isEqualTo("강승호");
        assertThat(response.getData().getMajor()).isEqualTo("IT학부, ICT융합학과");
        assertThat(response.getData().getClassNumber()).isEqualTo(201610302);
        assertThat(response.getData().getProfileUrl()).isEqualTo("http://profile.com");
    }

    @Test
    void 토큰을_이용해_내_정보를_불러올때_잘못된_토큰이면_401_에러가_발생() throws Exception {
        // when
        ApiResponse<MemberInfoResponse> response = memberTestController.getMyMemberInfo("wrong token");

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION.getCode());
    }

    @Test
    void 내정보를_수정한다() throws Exception {
        // given
        String name = "승호강";
        String profileUrl = "http://abc.com";
        MemberMajor major = MemberMajor.IT_COMPUTER;
        Integer classNumber = 201710302;

        UpdateMemberRequest request = UpdateMemberRequest.testBuilder()
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .classNumber(classNumber)
            .build();

        String token = memberTestController.getMockMemberToken();

        // when
        ApiResponse<MemberInfoResponse> response = memberTestController.updateMemberInfo(request, token);

        // then
        assertThat(response.getData().getEmail()).isEqualTo("will.seungho@gmail.com");
        assertThat(response.getData().getName()).isEqualTo(name);
        assertThat(response.getData().getMajor()).isEqualTo(major.getName());
        assertThat(response.getData().getClassNumber()).isEqualTo(classNumber);
        assertThat(response.getData().getProfileUrl()).isEqualTo(profileUrl);
    }

    @Test
    void 특정_유저의_정보를_불러온다() throws Exception {
        // given
        String token = memberTestController.getMockMemberToken();

        Member member = memberRepository.save(MemberCreator.create("ksh980212@gmail.com"));

        // when
        ApiResponse<MemberInfoResponse> response = memberTestController.getMemberInfo(member.getId(), token);

        // then
        assertThat(response.getData().getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getData().getName()).isEqualTo(member.getName());
        assertThat(response.getData().getMajor()).isEqualTo(member.getMajor().getName());
        assertThat(response.getData().getClassNumber()).isEqualTo(member.getClassNumber());
        assertThat(response.getData().getProfileUrl()).isEqualTo(member.getProfileUrl());
    }

}
