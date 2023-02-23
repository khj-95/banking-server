package com.numble.bankingserver.open.domain;

import com.numble.bankingserver.common.util.BaseTime;
import com.numble.bankingserver.open.dto.AccountDTO;
import com.numble.bankingserver.user.domain.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Account extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private String accountNumber;
    @Column(nullable = false)
    private long balance;

    @Builder
    private Account(User user, String accountNumber, long balance) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public static Account createAccount(AccountDTO accountDTO) {
        return Account.builder()
            .user(accountDTO.getUser())
            .accountNumber(accountDTO.getAccountNumber())
            .balance(accountDTO.getBalance())
            .build();
    }
}
