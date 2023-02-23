package com.numble.bankingserver.open.service;

import com.numble.bankingserver.open.domain.Account;
import com.numble.bankingserver.open.dto.AccountDTO;
import com.numble.bankingserver.open.repository.AccountRepository;
import com.numble.bankingserver.open.vo.OpenAccountVO;
import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OpenService {

    private final AccountRepository repository;
    private final UserRepository userRepository;

    public void openAccount(OpenAccountVO openAccountVO) {
        User user = userRepository.findByUserId(openAccountVO.getId())
            .orElseThrow(() -> new RuntimeException());

        AccountDTO accountDTO = AccountDTO.convertToAccountDTO(openAccountVO, user);

        while (repository.existsByAccountNumber(accountDTO.getAccountNumber())) {
            // 계좌번호 재생성
        }

        Account openAccount = Account.createAccount(accountDTO);

        repository.save(openAccount);
    }
}
