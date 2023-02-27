package com.numble.bankingserver.user.service;

import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.exception.UserAlreadyExistsException;
import com.numble.bankingserver.user.repository.UserRepository;
import com.numble.bankingserver.user.vo.JoinVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void joinUser(JoinVO joinVO) {
        if (repository.findByUserId(joinVO.getId()).isPresent()) {
            throw new UserAlreadyExistsException("이미 존재하는 아이디입니다.");
        }

        User user = User.createUser(joinVO);

        user.passwordEncode(passwordEncoder);
        repository.save(user);
    }
}
