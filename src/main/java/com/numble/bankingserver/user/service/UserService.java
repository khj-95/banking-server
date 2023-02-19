package com.numble.bankingserver.user.service;

import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.dto.JoinDTO;
import com.numble.bankingserver.user.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = repository.findByUserId(id)
            .orElseThrow(() -> new UsernameNotFoundException("해당 아이디가 존재하지 않습니다."));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUserId())
            .password(user.getPassword())
            .build();
    }

    public void joinUser(JoinDTO joinDTO) throws Exception {
        if (repository.findByUserId(joinDTO.getId()).isPresent()) {
            throw new Exception("이미 존재하는 아이디입니다.");
        }

        User user = User.createUser(joinDTO);

        user.passwordEncode(passwordEncoder);
        repository.save(user);
    }
}
