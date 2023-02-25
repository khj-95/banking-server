package com.numble.bankingserver.open.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
}