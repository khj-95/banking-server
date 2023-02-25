package com.numble.bankingserver.open.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.numble.bankingserver.open.vo.OpenAccountVO;
import com.numble.bankingserver.user.domain.User;
import java.security.Principal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountDTOTest {

    @Mock
    Principal principal;

    @Mock
    User user;

    @Test
    public void convertToAccountDTO_정상작동() {
        OpenAccountVO vo = new OpenAccountVO(principal, 1000);
        AccountDTO accountDTO = AccountDTO.builder()
            .user(user)
            .accountNumber(vo.getAccountNumber())
            .balance(1000)
            .build();

        assertThat(AccountDTO.convertToAccountDTO(vo, user)).isEqualTo(accountDTO);
    }
}