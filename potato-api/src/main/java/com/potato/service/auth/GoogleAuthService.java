package com.potato.service.auth;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import com.potato.external.google.GoogleApiCaller;
import com.potato.external.google.dto.response.GoogleAccessTokenResponse;
import com.potato.external.google.dto.response.GoogleUserInfoResponse;
import com.potato.service.auth.dto.request.AuthRequest;
import com.potato.service.auth.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import static com.potato.config.session.SessionConstants.AUTH_SESSION;

@RequiredArgsConstructor
@Service
public class GoogleAuthService {

    private final HttpSession httpSession;
    private final GoogleApiCaller googleApiCaller;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public AuthResponse handleGoogleAuthentication(AuthRequest request) {
        GoogleAccessTokenResponse tokenResponse = googleApiCaller.getGoogleAccessToken(request.getCode(), request.getRedirectUri());
        GoogleUserInfoResponse userInfoResponse = googleApiCaller.getGoogleUserProfileInfo(tokenResponse.getAccessToken());

        Member findMember = memberRepository.findMemberByEmail(userInfoResponse.getEmail());
        if (findMember == null) {
            return AuthResponse.signUp(userInfoResponse.getEmail(), userInfoResponse.getName(), userInfoResponse.getPicture());
        }
        httpSession.setAttribute(AUTH_SESSION, findMember.getId());
        return AuthResponse.login(httpSession.getId());
    }

}
