package com.numble.bankingserver.inquiry.vo;

import com.numble.bankingserver.open.domain.Account;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountVO {

    private String name;
    private String accountNumber;
    private long balance;

    @Builder
    private AccountVO(String name, String accountNumber, long balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public static AccountVO convertToAccountVO(Account account) {
        return AccountVO.builder()
            .name(account.getUser().getName())
            .accountNumber(account.getAccountNumber())
            .balance(account.getBalance())
            .build();
    }

    public static List<AccountVO> convertToAccountVOList(List<Account> accountList) {
        return accountList.stream()
            .map(AccountVO::convertToAccountVO)
            .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AccountVO) {
            AccountVO tmp = (AccountVO) obj;
            return name.equals(tmp.getName())
                && accountNumber.equals(tmp.getAccountNumber())
                && balance == tmp.getBalance();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, accountNumber, balance);
    }
}
