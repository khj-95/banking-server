package com.numble.bankingserver.open.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.numble.bankingserver.open.repository.AccountRepository;
import com.numble.bankingserver.open.vo.OpenAccountVO;
import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.repository.UserRepository;
import java.security.Principal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OpenServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    Principal principal;

    @InjectMocks
    OpenService openService;

    @Test
    public void openAccount_정상작동() {
        OpenAccountVO openAccountVO = new OpenAccountVO(principal, 1000);
        User user = User.builder().userId("userId").password("1234567890").name("User1").build();

        when(userRepository.findByUserId(any())).thenReturn(Optional.of(user));
        when(accountRepository.existsByAccountNumber(any())).thenReturn(false);

        assertDoesNotThrow(() -> openService.openAccount(openAccountVO));
    }

    @Test
    public void 중복된_계좌번호_존재시_계좌번호_재발급_후_개설_정상작동() {
        OpenAccountVO openAccountVO = new OpenAccountVO(principal, 1000);
        User user = User.builder().userId("userId").password("1234567890").name("User1").build();

        when(userRepository.findByUserId(any())).thenReturn(Optional.of(user));
        when(accountRepository.existsByAccountNumber(any())).thenReturn(true, false);

        assertDoesNotThrow(() -> openService.openAccount(openAccountVO));
    }
}