package com.numble.bankingserver.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.numble.bankingserver.user.domain.User;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/*
 * 회원 저장
 * 회원 저장시 아이디가 없으면 오류
 * 회원 저장시 비밀번호가 없으면 오류
 * 회원 저장시 이름이 없으면 오류
 * 회원 저장시 중복된 아이디가 있으면 오류
 * userId으로 회원 찾기 기능이 잘 작동하는지
 * existsByUserId가 잘 작동하는지
 */
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository repository;
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
    public void 회원저장_성공() throws Exception {
        //given
        User user = User.builder().userId("userId").password("1234567890").name("User1").build();

        //when
        User saveUser = repository.save(user);

        //then
        //아직 예외 클래스를 만들지 않았기에 RuntimeException으로 처리
        User findUser = repository.findById(saveUser.getId())
            .orElseThrow(() -> new RuntimeException("저장된 회원이 없습니다"));
        assertThat(findUser).isSameAs(saveUser);
        assertThat(findUser).isSameAs(user);
    }

    @Test
    public void 오류_회원가입시_아이디가_없음() throws Exception {
        //given
        User user = User.builder().password("1234567890").name("User1").build();

        //when, then
        assertThrows(Exception.class, () -> repository.save(user));
    }

    @Test
    public void 오류_회원가입시_비밀번호가_없음() throws Exception {
        //given
        User user = User.builder().userId("userId").name("User1").build();

        //when, then
        assertThrows(Exception.class, () -> repository.save(user));
    }

    @Test
    public void 오류_회원가입시_이름이_없음() throws Exception {
        //given
        User user = User.builder().userId("userId").password("1234567890").build();

        //when, then
        assertThrows(Exception.class, () -> repository.save(user));
    }

    @Test
    public void 오류_회원가입시_중복된_아이디가_있음() throws Exception {
        //given
        User user1 = User.builder().userId("userId").password("1234567890").name("User1").build();
        User user2 = User.builder().userId("userId").password("1111111111").name("User2").build();

        repository.save(user1);
        clear();
        //when, then
        assertThrows(Exception.class, () -> repository.save(user2));
    }

    @Test
    public void findByUserId_정상작동() throws Exception {
        //given
        String userId = "userId";
        User user1 = User.builder().userId("userId").password("1234567890").name("User1").build();
        repository.save(user1);
        clear();

        //when, then
        assertThat(repository.findByUserId(userId).get().getUserId()).isEqualTo(user1.getUserId());
        assertThat(repository.findByUserId(userId).get().getName()).isEqualTo(user1.getName());
        assertThat(repository.findByUserId(userId).get().getId()).isEqualTo(user1.getId());
        assertThrows(Exception.class,
            () -> repository.findByUserId(userId + "123").orElseThrow(() -> new Exception()));
    }

    @Test
    public void existsByUserId_정상작동() throws Exception {
        //given
        String userId = "userId";
        User user1 = User.builder().userId("userId").password("1234567890").name("User1").build();
        repository.save(user1);
        clear();

        //when, then
        assertThat(repository.existsByUserId(userId)).isTrue();
        assertThat(repository.existsByUserId(userId + "123")).isFalse();
    }
}