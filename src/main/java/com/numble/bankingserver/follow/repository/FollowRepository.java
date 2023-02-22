package com.numble.bankingserver.follow.repository;

import com.numble.bankingserver.follow.domain.Follow;
import com.numble.bankingserver.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    Page<Follow> findByFromUserUserId(String userId, PageRequest pageRequest);
}
