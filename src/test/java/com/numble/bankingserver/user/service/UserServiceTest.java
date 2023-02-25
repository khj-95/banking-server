package com.numble.bankingserver.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.dto.JoinVO;
import com.numble.bankingserver.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    public void joinUser_정상작동() {
        JoinVO joinVO = JoinVO.builder()
            .id("userId")
            .password("123456789")
            .name("user1")
            .build();

        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.joinUser(joinVO));
    }

    @Test
    public void 오류_존재하는_아이디() {
        JoinVO joinVO = JoinVO.builder()
            .id("userId")
            .password("123456789")
            .name("user1")
            .build();
        User user = new User(1, "userId", "1234567890", "User1", null);

        when(userRepository.findByUserId(any())).thenReturn(Optional.of(user));

        assertThatThrownBy(
            () -> userService.joinUser(joinVO)
        )
            .hasMessage("이미 존재하는 아이디입니다.");
    }
}