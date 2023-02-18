package com.numble.bankingserver.user.service;

import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.dto.JoinDTO;
import com.numble.bankingserver.user.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Transactional
    public void joinUser(JoinDTO joinDTO) {
        User user = User.createUser(joinDTO);
        repository.save(user);
    }
}
