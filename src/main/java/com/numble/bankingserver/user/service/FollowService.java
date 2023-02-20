package com.numble.bankingserver.user.service;

import com.numble.bankingserver.user.domain.Follow;
import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.repository.FollowRepository;
import com.numble.bankingserver.user.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FollowService {

    private final FollowRepository repository;
    private final UserRepository userRepository;

    public void follow(String followingId, String userId) throws Exception {
        User fromUser = userRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException());
        User toUser = userRepository.findByUserId(followingId)
            .orElseThrow(() -> new RuntimeException());

        if (!userRepository.existsByUserId(followingId)) {
            log.error("추가할 친구 정보가 유효하지 않습니다.");
            throw new Exception();
        }

        if (repository.existsByFromUserAndToUser(fromUser, toUser)) {
            log.error("이미 존재하는 친구입니다.");
            throw new Exception();
        }

        Follow follow = Follow.createFollow(fromUser, toUser);

        repository.save(follow);

        log.info("follow from {} to {}", userId, followingId);
    }
}
