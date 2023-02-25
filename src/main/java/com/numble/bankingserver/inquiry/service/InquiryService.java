package com.numble.bankingserver.inquiry.service;

import com.numble.bankingserver.inquiry.vo.AccountVO;
import com.numble.bankingserver.inquiry.vo.InquiryInfoVO;
import com.numble.bankingserver.open.domain.Account;
import com.numble.bankingserver.open.repository.AccountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryService {

    private final AccountRepository accountRepository;

    public List<AccountVO> inquiryAccountList(String userId) {
        List<Account> accountList = accountRepository.findByUserUserId(userId);

        return AccountVO.convertToAccountVOList(accountList);
    }

    public AccountVO inquiryAccount(InquiryInfoVO info) throws Exception {
        Account findAccount =
            accountRepository.findByUserUserIdAndAccountNumber(
                    info.getId(),
                    info.getAccountNumber())
                .orElseThrow(() -> new RuntimeException());
        return AccountVO.convertToAccountVO(findAccount);
    }
}
