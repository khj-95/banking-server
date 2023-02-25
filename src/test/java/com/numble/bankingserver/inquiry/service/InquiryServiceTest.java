package com.numble.bankingserver.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.numble.bankingserver.inquiry.vo.AccountVO;
import com.numble.bankingserver.inquiry.vo.InquiryInfoVO;
import com.numble.bankingserver.open.domain.Account;
import com.numble.bankingserver.open.repository.AccountRepository;
import com.numble.bankingserver.user.domain.User;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/*
 * inquiryAccountList가 잘 작동하는지
 * inquiryAccount가 잘 작동하는지
 * */
@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    Principal principal;

    @InjectMocks
    InquiryService inquiryService;

    @Test
    public void inquiryAccountList_정상작동() {
        List<Account> accountList = new ArrayList<>();
        User user1 = new User(1, "userId", "1234567890", "User1", null);
        Account account1 = new Account(1, user1, "1234-123-123456", 1000);
        Account account2 = new Account(2, user1, "1234-123-123457", 1000);
        accountList.add(account1);
        accountList.add(account2);

        List<AccountVO> accountVOList = new ArrayList<>();
        AccountVO accountVO1 = AccountVO.builder().name("User1").accountNumber("1234-123-123456")
            .balance(1000).build();
        AccountVO accountVO2 = AccountVO.builder().name("User1").accountNumber("1234-123-123457")
            .balance(1000).build();
        accountVOList.add(accountVO1);
        accountVOList.add(accountVO2);

        when(accountRepository.findByUserUserId(any())).thenReturn(accountList);

        assertThat(inquiryService.inquiryAccountList(any()))
            .usingRecursiveComparison()
            .isEqualTo(accountVOList);
    }

    @Test
    public void inquiryAccount_정상작동() throws Exception {
        InquiryInfoVO info = new InquiryInfoVO(principal, "1234-123-123456");

        User user = new User(1, "userId", "1234567890", "User1", null);
        Account account = new Account(1, user, "1234-123-123456", 1000);
        AccountVO accountVO = AccountVO.builder()
            .name("User1")
            .accountNumber("1234-123-123456")
            .balance(1000)
            .build();

        when(accountRepository.findByUserUserIdAndAccountNumber(any(), any()))
            .thenReturn(Optional.of(account));

        assertThat(inquiryService.inquiryAccount(info)).isEqualTo(accountVO);
    }
}
