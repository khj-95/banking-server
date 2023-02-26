package com.numble.bankingserver.follow.repository;

import com.numble.bankingserver.follow.domain.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFromUserUserIdAndToUserUserId(String fromUserId, String toUserId);

    Page<Follow> findByFromUserUserId(String userId, PageRequest pageRequest);
}
