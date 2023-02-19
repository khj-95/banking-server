package com.numble.bankingserver.common.conf;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    /*
     * csrf().disable() : REST API는 stateless 하기 때문에 Cross-site request forgery 공격을 막는 코드가 필요없다.
     * headers().frameOptions().disable() : h2-console 화면을 사용하기 위해서 비활성화
     * cors() : 프론트엔드 서버와 통신을 위해 설정
     * sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) : 스프링 시큐리티가 세션을 만들지도 기존 것을 사용하지도 않음.
     * authorizeRequests().antMatchers("/login/**", "/user", "/auth/**").permitAll() : 인증 절차 없이 허용할 URI
     * anyRequest().authenticated() : 위에서 허용한 URI를 제외한 모든 요청은 인증을 완료해야 접근 가능
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .headers().frameOptions().disable()
            .and()
            .cors()
            .and()
            .sessionManagement()//세션 정책 설정
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/login/**", "/user", "/auth/**")
            .permitAll()
            .anyRequest().authenticated()
        ;

        return http.build();
    }
}
