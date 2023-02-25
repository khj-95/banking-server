package com.numble.bankingserver.open.domain;

import com.numble.bankingserver.common.util.BaseTime;
import com.numble.bankingserver.open.dto.AccountDTO;
import com.numble.bankingserver.user.domain.User;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Table(name = "account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Slf4j
public class Account extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    // 계좌번호 : 3자리-2자리-6자리 형태
    @Column(nullable = false, unique = true)
    private String accountNumber;
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

    @PrePersist
    private void checkBalance() {
        if (this.balance < 0) {
            log.info("잔액은 음수일 수 없습니다.");
            throw new RuntimeException();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            Account tmp = (Account) obj;
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
