package com.numble.bankingserver.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.numble.bankingserver.user.dto.JoinVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserTest {

    @Mock
    User user;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void createUser() {
        user = User.builder().userId("userId").name("user1").password("123456789").build();
    }

    @Test
    public void createUser_정상작동() {
        JoinVO joinVO = new JoinVO("userId", "123456789", "user1");
        User user = User.builder().userId("userId").name("user1").password("123456789").build();

        assertThat(User.createUser(joinVO)).isEqualTo(user);
    }

    @Test
    public void passwordEncode_정상작동() {
        String previousPassword = user.getPassword();

        user.passwordEncode(passwordEncoder);

        assertThat(user.getPassword().equals(previousPassword)).isFalse();
    }

}