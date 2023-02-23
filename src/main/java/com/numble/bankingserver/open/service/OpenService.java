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

    public void openAccount(OpenAccountVO vo) {
        User user = userRepository.findByUserId(vo.getId())
            .orElseThrow(() -> new RuntimeException());

        Account openAccount = Account.createAccount(AccountDTO.createAccountDTO(vo, user));

        repository.save(openAccount);
    }
}
