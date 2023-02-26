package com.numble.bankingserver.transfer.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferVOTest {

    @Mock
    Principal principal;

    @BeforeEach
    public void setPrincipal() {
        when(principal.getName()).thenReturn("fromUser");
    }

    @Test
    public void createTransferVO가_정상작동() {
        TransferVO transferVO = TransferVO.builder()
            .principal(principal)
            .fromAccountNumber("1234-567-891011")
            .toUser("toUser")
            .toAccountNumber("1234-567-891012")
            .amount(1)
            .build();

        assertThat(TransferVO.createTransferVO(
            principal, "toUser", "1234-567-891011", "1234-567-891012", 1))
            .isEqualTo(transferVO);
    }
}