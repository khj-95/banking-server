package com.numble.bankingserver.follow.service;

import com.numble.bankingserver.follow.domain.Follow;
import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.follow.repository.FollowRepository;
import com.numble.bankingserver.user.repository.UserRepository;
import com.numble.bankingserver.follow.vo.FollowVO;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FollowService {

    private final String FOLLOW_ERROR = "FOLLOW ERROR";
    private final FollowRepository repository;
    private final UserRepository userRepository;

    public void follow(String followingId, String userId) throws Exception {
        User fromUser = getUser(userId, "요청자 정보가 유효하지 않습니다.");
        User toUser = getUser(followingId, "추가할 친구 정보가 유효하지 않습니다.");

        checkValidFollowRequest(fromUser, toUser);

        Follow follow = Follow.createFollow(fromUser, toUser);

        repository.save(follow);

        log.info("follow from {} to {}", userId, followingId);
    }


    public Page<FollowVO> lookUpFollowList(int page, String userId) {
        PageRequest pageRequest = PageRequest.of(page, 20);
        Page<Follow> followList = repository.findByFromUserUserId(userId, pageRequest);
        return new PageImpl<>(
            FollowVO.toFollowList(followList), pageRequest, followList.getTotalElements()
        );
    }

    private User getUser(String userId, String message) {
        return userRepository.findByUserId(userId)
            .orElseThrow(() -> {
                log.error(FOLLOW_ERROR);
                return new RuntimeException(message);
            });
    }

    private void checkValidFollowRequest(User fromUser, User toUser) throws Exception {
        checkFriendNotEqualsUser(fromUser, toUser);
        checkNotExistedFriend(fromUser, toUser);
    }

    private void checkFriendNotEqualsUser(User fromUser, User toUser) throws Exception {
        if (fromUser.equals(toUser)) {
            log.error(FOLLOW_ERROR);
            throw new Exception("유효하지 않은 친구 추가 요청입니다.");
        }
    }

    private void checkNotExistedFriend(User fromUser, User toUser) throws Exception {
        if (repository.existsByFromUserAndToUser(fromUser, toUser)) {
            log.error(FOLLOW_ERROR);
            throw new Exception("이미 존재하는 친구입니다.");
        }
    }
}
