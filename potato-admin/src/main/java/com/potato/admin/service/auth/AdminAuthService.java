package com.potato.admin.service.auth;

import com.potato.admin.config.session.AdminSession;
import com.potato.admin.config.session.SessionConstants;
import com.potato.admin.service.auth.dto.request.GoogleAuthRequest;
import com.potato.domain.domain.administrator.Administrator;
import com.potato.domain.domain.administrator.AdministratorRepository;
import com.potato.external.external.google.GoogleApiCaller;
import com.potato.external.external.google.dto.response.GoogleAccessTokenResponse;
import com.potato.external.external.google.dto.response.GoogleUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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

        httpSession.setAttribute(SessionConstants.AUTH_SESSION, AdminSession.of(administrator.getId()));
        return httpSession.getId();
    }

}
