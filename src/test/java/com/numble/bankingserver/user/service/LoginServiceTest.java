package com.numble.bankingserver.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    LoginService loginService;

    @Test
    public void loadUserByUsername_정상작동() {
        String id = "userId";
        User user = User.builder().userId(id).password("123456789").name("user1").build();
        UserDetails returnUser =
            org.springframework.security.core.userdetails.User.builder()
                .username(id)
                .password("123456789")
                .roles("DEFAULT")
                .build();

        when(userRepository.findByUserId(id)).thenReturn(Optional.of(user));

        assertThat(loginService.loadUserByUsername(id)).isEqualTo(returnUser);
    }

    @Test
    public void 오류_존재하지_않는_아이디() {
        String id = "userId";
        UserDetails returnUser =
            org.springframework.security.core.userdetails.User.builder()
                .username(id)
                .password("123456789")
                .roles("DEFAULT")
                .build();

        when(userRepository.findByUserId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(
            () -> loginService.loadUserByUsername(id)
        )
            .hasMessage("해당 아이디가 존재하지 않습니다.");
    }
}