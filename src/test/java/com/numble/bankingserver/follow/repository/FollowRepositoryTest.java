package com.numble.bankingserver.follow.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.numble.bankingserver.follow.domain.Follow;
import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.repository.UserRepository;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/*
 * 친구 추가
 * 친구 추가시 유저 객체가 없으면 오류
 * 친구 추가시 추기할 유저 객체가 없으면 오류
 * 친구 추가시 중복된 친구 관계면 오류
 * existsByFromUserAndToUser가 잘 작동하는지
 * findByFromUserId가 잘 작동하는지
 */
@SpringBootTest
@Transactional
class FollowRepositoryTest {

    @Autowired
    FollowRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    @AfterEach
    private void after() {
        em.clear();
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    @Test
    public void 친구추가_성공() throws Exception {
        //given
        User user1 = User.builder().userId("userId1").password("1234567890").name("User1").build();
        User user2 = User.builder().userId("userId2").password("1234567890").name("User2").build();

        userRepository.save(user1);
        userRepository.save(user2);
        clear();

        Follow follow = Follow.builder().fromUser(user1).toUser(user2).build();

        //when
        Follow saveFollow = repository.save(follow);

        //then
        //아직 예외 클래스를 만들지 않았기에 RuntimeException으로 처리
        Follow findFollow = repository.findById(saveFollow.getId())
            .orElseThrow(() -> new RuntimeException("추가된 친구가 없습니다."));
        assertThat(findFollow).isSameAs(saveFollow);
        assertThat(findFollow).isSameAs(follow);
    }

    @Test
    public void 오류_친구추가시_유저객체가_없음() throws Exception {
        User user = User.builder().userId("userId1").password("1234567890").name("User1").build();

        userRepository.save(user);
        clear();

        //given
        Follow follow = Follow.builder().toUser(user).build();

        //when, then
        assertThrows(Exception.class, () -> repository.save(follow));
    }

    @Test
    public void 오류_친구추가시_추가할유저객체가_없음() throws Exception {
        User user = User.builder().userId("userId1").password("1234567890").name("User1").build();

        userRepository.save(user);
        clear();

        //given
        Follow follow = Follow.builder().fromUser(user).build();

        //when, then
        assertThrows(Exception.class, () -> repository.save(follow));
    }

    @Test
    public void 오류_친구추가시_중복된_친구관계() throws Exception {
        //given
        User user1 = User.builder().userId("userId1").password("1234567890").name("User1").build();
        User user2 = User.builder().userId("userId2").password("1234567890").name("User2").build();

        userRepository.save(user1);
        userRepository.save(user2);
        clear();

        Follow follow1 = Follow.builder().fromUser(user1).toUser(user2).build();

        repository.save(follow1);
        clear();

        Follow follow2 = Follow.builder().fromUser(user1).toUser(user2).build();

        //when, then
        assertThrows(Exception.class, () -> repository.save(follow2));
    }

    @Test
    public void existsByFromUserAndToUser_정상작동() {
        User user1 = User.builder().userId("userId1").password("1234567890").name("User1").build();
        User user2 = User.builder().userId("userId2").password("1234567890").name("User2").build();

        userRepository.save(user1);
        userRepository.save(user2);
        clear();

        Follow follow = Follow.builder().fromUser(user1).toUser(user2).build();

        repository.save(follow);
        clear();

        assertThat(repository.existsByFromUserAndToUser(user1, user2)).isTrue();
        assertThat(repository.existsByFromUserAndToUser(user2, user1)).isFalse();
    }

    @Test
    public void findByFromUserId_정상작동() {
        User user1 = User.builder().userId("userId1").password("1234567890").name("User1").build();
        User user2 = User.builder().userId("userId2").password("1234567890").name("User2").build();
        User user3 = User.builder().userId("userId3").password("1234567890").name("User3").build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        clear();

        Follow follow1 = Follow.builder().fromUser(user1).toUser(user2).build();
        Follow follow2 = Follow.builder().fromUser(user1).toUser(user3).build();
        Follow follow3 = Follow.builder().fromUser(user2).toUser(user3).build();

        repository.save(follow1);
        repository.save(follow2);
        repository.save(follow3);
        clear();

        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Follow> followList = repository.findByFromUserUserId("userId1", pageRequest);

        Follow first = followList.getContent().get(0);
        Follow second = followList.getContent().get(1);

        assertThat(followList.getTotalElements()).isEqualTo(2);
        assertThat(first).isEqualTo(follow1);
        assertThat(second).isEqualTo(follow2);
    }
}