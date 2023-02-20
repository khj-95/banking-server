package com.numble.bankingserver.user.repository;

import com.numble.bankingserver.user.domain.Follow;
import com.numble.bankingserver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFromUserAndToUser(User fromUser, User toUser);
}
