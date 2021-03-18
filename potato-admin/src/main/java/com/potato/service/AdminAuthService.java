package com.potato.service;

import com.potato.domain.adminMember.AdminMember;
import com.potato.domain.adminMember.AdminMemberRepository;
import com.potato.exception.NotFoundException;
import com.potato.external.google.GoogleApiCaller;
import com.potato.external.google.dto.response.GoogleAccessTokenResponse;
import com.potato.external.google.dto.response.GoogleUserInfoResponse;
import com.potato.service.dto.request.GoogleAuthRequest;
import com.potato.session.AdminMemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.potato.session.SessionConstants.AUTH_SESSION;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final HttpSession httpSession;
    private final GoogleApiCaller googleApiCaller;
    private final AdminMemberRepository adminMemberRepository;

    public String handleGoogleAuthentication(GoogleAuthRequest request) {
        GoogleAccessTokenResponse googleAccessToken = googleApiCaller.getGoogleAccessToken(request.getCode(), request.getRedirectUri());
        GoogleUserInfoResponse googleAdminProfileInfo = googleApiCaller.getGoogleUserProfileInfo(googleAccessToken.getAccessToken());

        AdminMember adminMember = adminMemberRepository.findByEmail(googleAdminProfileInfo.getEmail());

        if (adminMember == null) {
            throw new NotFoundException(String.format("존재하지 않는 (%s)의 관리자입니다", googleAdminProfileInfo.getEmail()));
        }
        httpSession.setAttribute(AUTH_SESSION, AdminMemberSession.of(adminMember.getId()));
        return httpSession.getId();
    }

}
