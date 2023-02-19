package com.numble.bankingserver.common.security.handler;

import com.numble.bankingserver.common.security.JwtService;
import com.numble.bankingserver.user.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository repository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        // 인증 정보에서 Username(email) 추출
        String id = extractUsername(authentication);
        // JwtService의 createAccessToken을 사용하여 AccessToken 발급
        String accessToken = jwtService.createAccessToken(id);
        // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급
        String refreshToken = jwtService.createRefreshToken();

        // 응답 헤더에 AccessToken, RefreshToken 실어서 응답
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        repository.findByUserId(id)
            .ifPresent(user -> {
                user.updateRefreshToken(refreshToken);
                repository.saveAndFlush(user);
            });
        log.info("로그인에 성공하였습니다. 아이디 : {}", id);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
