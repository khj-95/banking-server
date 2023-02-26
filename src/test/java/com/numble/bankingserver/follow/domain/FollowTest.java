package com.numble.bankingserver.follow.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.numble.bankingserver.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FollowTest {

    @Test
    public void createFollow_정상작동() {
        User fromUser = User.builder().userId("userId1").password("1234567890").name("User1")
            .build();
        User toUser = User.builder().userId("userId2").password("1234567890").name("User2").build();

        Follow follow = Follow.builder()
            .fromUser(fromUser)
            .toUser(toUser)
            .build();

        assertThat(Follow.createFollow(fromUser, toUser)).isEqualTo(follow);
    }
}