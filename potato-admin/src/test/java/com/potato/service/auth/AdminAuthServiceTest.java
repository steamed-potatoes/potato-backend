package com.potato.service.auth;

import com.potato.domain.administrator.Administrator;
import com.potato.domain.administrator.AdministratorCreator;
import com.potato.domain.administrator.AdministratorRepository;
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
    private AdministratorRepository administratorRepository;

    @Autowired
    private HttpSession httpSession;

    @BeforeEach
    void setUpAdminGoogleAuth() {
        adminAuthService = new AdminAuthService(httpSession, new StubAdminGoogleApiCaller(), administratorRepository);
    }

    @AfterEach
    void cleanUp() {
        administratorRepository.deleteAll();
    }

    @Test
    void 구글_인증시_존재하는_이메일이면_로그인_처리한다() {
        //given
        Administrator administrator = AdministratorCreator.create("googleAuth@gmail.com", "googleAuth");
        administratorRepository.save(administrator);

        //when
        GoogleAuthRequest request = GoogleAuthRequest.testInstance("code", "redirectUri");
        String token = adminAuthService.handleGoogleAuthentication(request);

        //then
        assertThat(token).isNotNull();
    }

    @Test
    void 구글_인증시_존재하지_않는_이메일이면_애러가_발생() {
        //given
        Administrator administrator = AdministratorCreator.create("googleWrong@gmail.com", "googleAuth");
        administratorRepository.save(administrator);

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
