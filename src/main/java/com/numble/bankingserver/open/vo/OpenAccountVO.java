package com.numble.bankingserver.open.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.security.Principal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OpenAccountVO {

    private String id;
    private String accountNumber;
    @Builder.Default
    private long balance = 0;

    @JsonCreator
    @Builder
    public OpenAccountVO(Principal principal, @JsonProperty("account-number") String accountNumber,
        @JsonProperty("balance") long balance) {
        this.id = principal.getName();
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
