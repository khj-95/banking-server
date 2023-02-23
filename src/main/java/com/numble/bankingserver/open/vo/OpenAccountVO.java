package com.numble.bankingserver.open.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.numble.bankingserver.open.dto.Accounts;
import java.security.Principal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OpenAccountVO {

    private String id;
    private String accountNumber;
    private long balance;

    @JsonCreator
    @Builder
    public OpenAccountVO(Principal principal, @JsonProperty("balance") long balance) {
        this.id = principal.getName();
        this.accountNumber = Accounts.createAccountNumber();
        this.balance = balance;
    }
}
