package com.numble.bankingserver.open.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.numble.bankingserver.open.dto.AccountDTO;
import com.numble.bankingserver.open.vo.OpenAccountVO;
import com.numble.bankingserver.user.domain.User;
import java.security.Principal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountTest {

    @Mock
    Principal principal;

    @Mock
    User user;

    @Test
    public void createAccount_정상작동() {
        OpenAccountVO openAccountVO = new OpenAccountVO(principal, 1000);
        AccountDTO accountDTO = AccountDTO.convertToAccountDTO(openAccountVO, user);
        Account account = Account.builder()
            .user(user)
            .accountNumber(openAccountVO.getAccountNumber())
            .balance(1000)
            .build();

        assertThat(Account.createAccount(accountDTO)).isEqualTo(account);
    }

    @Test
    public void deposit가_정상작동() {
        Account account = Account.builder()
            .user(user)
            .accountNumber("1234-567-891011")
            .balance(1000)
            .build();

        assertDoesNotThrow(() -> account.deposit(10000));
        assertTrue(account.getBalance() == 11000);
    }

    @Test
    public void 오류_입금시_한도초과() {
        Account account = Account.builder()
            .user(user)
            .accountNumber("1234-567-891011")
            .balance(Long.MAX_VALUE)
            .build();

        assertThatThrownBy(() -> account.deposit(10000))
            .hasMessage("한도 초과");
    }

    @Test
    public void withdraw가_정상작동() {
        Account account = Account.builder()
            .user(user)
            .accountNumber("1234-567-891011")
            .balance(1000)
            .build();

        assertDoesNotThrow(() -> account.withdraw(1000));
        assertTrue(account.getBalance() == 0);
    }

    @Test
    public void 오류_출금시_잔액부족() {
        Account account = Account.builder()
            .user(user)
            .accountNumber("1234-567-891011")
            .balance(1000)
            .build();

        assertThatThrownBy(() -> account.withdraw(10000))
            .hasMessage("잔액 부족");
    }
}