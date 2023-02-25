package com.numble.bankingserver.user.service;

import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.dto.JoinVO;
import com.numble.bankingserver.user.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void joinUser(JoinVO joinVO) throws Exception {
        if (repository.findByUserId(joinVO.getId()).isPresent()) {
            throw new Exception("이미 존재하는 아이디입니다.");
        }

        User user = User.createUser(joinVO);

        user.passwordEncode(passwordEncoder);
        repository.save(user);
    }
}
