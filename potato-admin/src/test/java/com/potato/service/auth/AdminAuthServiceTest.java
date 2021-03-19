package com.potato.service.auth;

import com.potato.domain.adminMember.AdminMember;
import com.potato.domain.adminMember.AdminMemberCreator;
import com.potato.domain.adminMember.AdminMemberRepository;
import com.potato.exception.NotFoundException;
import com.potato.external.google.GoogleApiCaller;
import com.potato.external.google.dto.response.GoogleAccessTokenResponse;
import com.potato.external.google.dto.response.GoogleUserInfoResponse;
import com.potato.service.auth.dto.request.GoogleAuthRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AdminAuthServiceTest {

    private AdminAuthService adminAuthService;

    @Autowired
    private AdminMemberRepository adminMemberRepository;

    @Autowired
    private HttpSession httpSession;

    @BeforeEach
    void setUpAdminGoogleAuth() {
        adminAuthService = new AdminAuthService(httpSession, new StubAdminGoogleApiCaller(), adminMemberRepository);
    }

    @AfterEach
    void cleanUp() {
        adminMemberRepository.deleteAll();
    }

    @Test
    void 구글_인증시_존재하는_이메일이면_로그인_처리한다() {
        //given
        AdminMember adminMember = AdminMemberCreator.create("googleAuth@gmail.com", "googleAuth");
        adminMemberRepository.save(adminMember);

        //when
        GoogleAuthRequest request = GoogleAuthRequest.testInstance("code", "redirectUri");
        String token = adminAuthService.handleGoogleAuthentication(request);

        //then
        assertThat(token).isNotNull();
    }

    @Test
    void 구글_인증시_존재하지_않는_이메일이면_애러가_발생() {
        //given
        AdminMember adminMember = AdminMemberCreator.create("googleWrong@gmail.com", "googleAuth");
        adminMemberRepository.save(adminMember);

        GoogleAuthRequest request = GoogleAuthRequest.testInstance("code", "redirectUri");

        // when & then
        assertThatThrownBy(
            () -> adminAuthService.handleGoogleAuthentication(request)
        ).isInstanceOf(NotFoundException.class);
    }

    private static class StubAdminGoogleApiCaller implements GoogleApiCaller {

        @Override
        public GoogleAccessTokenResponse getGoogleAccessToken(String code, String redirectUri) {
            return GoogleAccessTokenResponse.testBuilder()
                .accessToken("accessToken")
                .build();
        }

        @Override
        public GoogleUserInfoResponse getGoogleUserProfileInfo(String accessToken) {
            return GoogleUserInfoResponse.testBuilder()
                .email("googleAuth@gmail.com")
                .name("googleAuth")
                .build();
        }

    }

}
