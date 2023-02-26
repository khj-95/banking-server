package com.numble.bankingserver.inquiry.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.numble.bankingserver.open.domain.Account;
import com.numble.bankingserver.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountVOTest {

    User user;

    @BeforeEach
    public void setUser() {
        user = User.builder()
            .userId("userId")
            .password("1234")
            .name("user1")
            .build();
    }

    @Test
    public void convertToAccountVO가_정상작동() {
        Account account = Account.builder()
            .user(user)
            .accountNumber("1234-567-891011")
            .balance(1000)
            .build();

        AccountVO accountVO = AccountVO.builder()
            .name("user1")
            .accountNumber("1234-567-891011")
            .balance(1000)
            .build();

        assertThat(AccountVO.convertToAccountVO(account))
            .isEqualTo(accountVO);
    }

    @Test
    public void convertToAccountVOList가_정상작동() {
        List<Account> accountList = new ArrayList<>();
        Account account1 = Account.builder()
            .user(user)
            .accountNumber("1234-567-891011")
            .balance(1000)
            .build();
        Account account2 = Account.builder()
            .user(user)
            .accountNumber("1234-567-891012")
            .balance(10000)
            .build();
        accountList.add(account1);
        accountList.add(account2);

        List<AccountVO> accountVOList = new ArrayList<>();
        AccountVO accountVO1 = AccountVO.builder()
            .name("user1")
            .accountNumber("1234-567-891011")
            .balance(1000)
            .build();
        AccountVO accountVO2 = AccountVO.builder()
            .name("user1")
            .accountNumber("1234-567-891012")
            .balance(10000)
            .build();
        accountVOList.add(accountVO1);
        accountVOList.add(accountVO2);

        assertThat(AccountVO.convertToAccountVOList(accountList))
            .usingRecursiveComparison()
            .isEqualTo(accountVOList);
    }
}