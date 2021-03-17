package com.potato.service;

import com.potato.domain.AdminMember;
import com.potato.domain.AdminMemberRepository;
import com.potato.exception.NotFoundException;
import com.potato.external.google.GoogleApiCaller;
import com.potato.external.google.dto.response.GoogleAccessTokenResponse;
import com.potato.external.google.dto.response.GoogleUserInfoResponse;
import com.potato.service.dto.request.GoogleAuthRequest;
import com.potato.service.dto.response.AdminAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminMemberRepository adminMemberRepository;
    private final HttpSession httpSession;

    private final GoogleApiCaller googleApiCaller;

    //google login step5
    // 인가 코드를 받은 뒤 인가 코드로 토큰 받는 코드 -> post 방식
    // googleAccessToken 안에는 post로 보낸것들의 응답이 들어있음 (access_token, expires_in, refresh_token, scope, token_type(Bearer)
    public AdminAuthResponse handlerGoogleAuth(GoogleAuthRequest request) {
        GoogleAccessTokenResponse googleAccessToken = googleApiCaller.getGoogleAccessToken(request.getCode(), request.getRedirectUri());
        GoogleUserInfoResponse googleAdminProfileInfo = googleApiCaller.getGoogleUserProfileInfo(googleAccessToken.getAccessToken());

        AdminMember adminMember = adminMemberRepository.findByEmail(googleAdminProfileInfo.getEmail());

        if (adminMember == null) {
            throw new NotFoundException(String.format("존재하지 않는 (%s)의 관리자입니다", adminMember.getEmail()));
        }
        httpSession.setAttribute("adminMember", adminMember);
        return AdminAuthResponse.login(httpSession.getId());
    }

}
