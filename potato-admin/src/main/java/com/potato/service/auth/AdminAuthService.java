package com.potato.service.auth;

import com.potato.domain.administrator.Administrator;
import com.potato.domain.administrator.AdministratorRepository;
import com.potato.external.google.GoogleApiCaller;
import com.potato.external.google.dto.response.GoogleAccessTokenResponse;
import com.potato.external.google.dto.response.GoogleUserInfoResponse;
import com.potato.service.auth.dto.request.GoogleAuthRequest;
import com.potato.config.session.AdminMemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.potato.config.session.SessionConstants.AUTH_SESSION;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final HttpSession httpSession;
    private final GoogleApiCaller googleApiCaller;
    private final AdministratorRepository administratorRepository;

    public String handleGoogleAuthentication(GoogleAuthRequest request) {
        GoogleAccessTokenResponse googleAccessToken = googleApiCaller.getGoogleAccessToken(request.getCode(), request.getRedirectUri());
        GoogleUserInfoResponse googleAdminProfileInfo = googleApiCaller.getGoogleUserProfileInfo(googleAccessToken.getAccessToken());

        Administrator administrator = AdminAuthServiceUtils.findAdminMemberByEmail(administratorRepository, googleAdminProfileInfo.getEmail());

        httpSession.setAttribute(AUTH_SESSION, AdminMemberSession.of(administrator.getId()));
        return httpSession.getId();
    }

}
