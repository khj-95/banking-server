package com.numble.bankingserver.common.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.bankingserver.common.security.JwtService;
import com.numble.bankingserver.common.security.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.numble.bankingserver.common.security.filter.JwtAuthenticationProcessingFilter;
import com.numble.bankingserver.common.security.handler.LoginFailureHandler;
import com.numble.bankingserver.common.security.handler.LoginSuccessHandler;
import com.numble.bankingserver.user.repository.UserRepository;
import com.numble.bankingserver.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final LoginService service;
    private final JwtService jwtService;
    private final UserRepository repository;
    private final ObjectMapper objectMapper;

    /*
     * formLogin().disable() : Spring Security에서 기본으로 FormLogin 형식의 로그인을 제공, 자체 Login으로 로그인을 진행
     * httpBasic().disable() : JWT 토큰을 사용한 로그인(Bearer 방식) -> 기본 설정인 httpBasic은 비활성화
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
            .formLogin().disable() // FormLogin 사용 X
            .httpBasic().disable() // httpBasic 사용 X
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
            .antMatchers("/join")
            .permitAll() // 회원가입 접근 가능
            .anyRequest().authenticated()
        ;

        // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
        // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(),
            CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // PasswordEncode로 BCryptPasswordEncoder 사용
    }

    /**
     * AuthenticationManager 설정 후 등록 PasswordEncoder를 사용하는 AuthenticationProvider 지정
     * (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용) FormLogin(기존 스프링 시큐리티 로그인)과 동일하게
     * DaoAuthenticationProvider 사용 UserDetailsService는 커스텀 LoginService로 등록 또한, FormLogin과 동일하게
     * AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(service);
        return new ProviderManager(provider);
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, repository);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한
     * AuthenticationManager(ProviderManager) 설정 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한
     * handler 설정
     */
    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
            = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(
            loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(
            loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(
            jwtService, repository);
        return jwtAuthenticationFilter;
    }
}
