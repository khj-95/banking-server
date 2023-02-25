package com.numble.bankingserver.open.dto;

import com.numble.bankingserver.open.vo.OpenAccountVO;
import com.numble.bankingserver.user.domain.User;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountDTO {

    private User user;
    private String accountNumber;
    private long balance;

    @Builder
    private AccountDTO(User user, String accountNumber, long balance) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public static AccountDTO convertToAccountDTO(OpenAccountVO vo, User user) {
        return AccountDTO.builder()
            .user(user)
            .accountNumber(vo.getAccountNumber())
            .balance(vo.getBalance())
            .build();
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AccountDTO) {
            AccountDTO tmp = (AccountDTO) obj;
            return user.equals(tmp.getUser())
                && accountNumber.equals(tmp.getAccountNumber())
                && balance == tmp.getBalance();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, accountNumber, balance);
    }
}
