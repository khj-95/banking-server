package com.numble.bankingserver.open.dto;

import com.numble.bankingserver.open.vo.OpenAccountVO;
import com.numble.bankingserver.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountDTO {

    private User user;
    private String accountNumber;
    private long balance;

    public static AccountDTO createAccountDTO(OpenAccountVO vo, User user) {
        return AccountDTO.builder()
            .user(user)
            .accountNumber(vo.getAccountNumber())
            .balance(vo.getBalance())
            .build();
    }
}
