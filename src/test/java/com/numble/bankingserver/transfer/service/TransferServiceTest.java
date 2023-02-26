package com.numble.bankingserver.transfer.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.numble.bankingserver.follow.repository.FollowRepository;
import com.numble.bankingserver.open.domain.Account;
import com.numble.bankingserver.open.repository.AccountRepository;
import com.numble.bankingserver.transfer.vo.TransferVO;
import com.numble.bankingserver.user.domain.User;
import java.security.Principal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    FollowRepository followRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    User user;

    @Mock
    Principal principal;

    @InjectMocks
    TransferService transferService;

    @BeforeEach
    public void setPrincipal() {
        when(principal.getName()).thenReturn("fromUser");
    }

    @Test
    public void transfer가_정상작동() {
        TransferVO transferVO = TransferVO.builder()
            .principal(principal)
            .toUser("toUser")
            .fromAccountNumber("1234-567-891011")
            .toAccountNumber("1234-567-891012")
            .amount(1000)
            .build();

        Account fromUserAccount = Account.builder()
            .user(user)
            .accountNumber("1234-567-891011")
            .balance(1000)
            .build();
        Account toUserAccount = Account.builder()
            .user(user)
            .accountNumber("1234-567-891012")
            .balance(1000)
            .build();

        when(followRepository.existsByFromUserUserIdAndToUserUserId(any(), any()))
            .thenReturn(true, true);
        when(accountRepository.findByUserUserIdAndAccountNumber(any(), any()))
            .thenReturn(Optional.of(fromUserAccount), Optional.of(toUserAccount));

        assertDoesNotThrow(() -> transferService.transfer(transferVO));
        assertTrue(fromUserAccount.getBalance() == 0);
        assertTrue(toUserAccount.getBalance() == 2000);
    }

    @Test
    public void 오류_친구_아님() {
        TransferVO transferVO = TransferVO.builder()
            .principal(principal)
            .toUser("toUser")
            .fromAccountNumber("1234-567-891011")
            .toAccountNumber("1234-567-891012")
            .amount(1000)
            .build();

        when(followRepository.existsByFromUserUserIdAndToUserUserId(any(), any()))
            .thenReturn(false, false);

        assertThatThrownBy(() -> transferService.transfer(transferVO))
            .hasMessage("친구끼리만 계좌 이체가 가능합니다.");
    }

    @Test
    public void 오류_단방향_친구() {
        TransferVO transferVO = TransferVO.builder()
            .principal(principal)
            .toUser("toUser")
            .fromAccountNumber("1234-567-891011")
            .toAccountNumber("1234-567-891012")
            .amount(1000)
            .build();

        when(followRepository.existsByFromUserUserIdAndToUserUserId(any(), any()))
            .thenReturn(true, false);

        assertThatThrownBy(() -> transferService.transfer(transferVO))
            .hasMessage("친구끼리만 계좌 이체가 가능합니다.");

        when(followRepository.existsByFromUserUserIdAndToUserUserId(any(), any()))
            .thenReturn(false, true);

        assertThatThrownBy(() -> transferService.transfer(transferVO))
            .hasMessage("친구끼리만 계좌 이체가 가능합니다.");
    }

    @Test
    public void 오류_유저의_계좌_정보가_유효하지_않음() {
        TransferVO transferVO = TransferVO.builder()
            .principal(principal)
            .toUser("toUser")
            .fromAccountNumber("1234-567-891011")
            .toAccountNumber("1234-567-891012")
            .amount(1000)
            .build();

        Account toUserAccount = Account.builder()
            .user(user)
            .accountNumber("1234-567-891012")
            .balance(1000)
            .build();

        when(followRepository.existsByFromUserUserIdAndToUserUserId(any(), any()))
            .thenReturn(true, true);
        when(accountRepository.findByUserUserIdAndAccountNumber(any(), any()))
            .thenReturn(Optional.empty(), Optional.of(toUserAccount));

        assertThatThrownBy(() -> transferService.transfer(transferVO))
            .hasMessage("유효하지 않은 유저의 계좌 정보입니다.");
    }

    @Test
    public void 오류_친구의_계좌_정보가_유효하지_않음() {
        TransferVO transferVO = TransferVO.builder()
            .principal(principal)
            .toUser("toUser")
            .fromAccountNumber("1234-567-891011")
            .toAccountNumber("1234-567-891012")
            .amount(1000)
            .build();

        Account fromUserAccount = Account.builder()
            .user(user)
            .accountNumber("1234-567-891011")
            .balance(1000)
            .build();

        when(followRepository.existsByFromUserUserIdAndToUserUserId(any(), any()))
            .thenReturn(true, true);
        when(accountRepository.findByUserUserIdAndAccountNumber(any(), any()))
            .thenReturn(Optional.of(fromUserAccount), Optional.empty());

        assertThatThrownBy(() -> transferService.transfer(transferVO))
            .hasMessage("유효하지 않은 친구의 계좌 정보입니다.");
    }
}