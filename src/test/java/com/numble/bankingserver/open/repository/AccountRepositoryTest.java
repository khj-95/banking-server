package com.numble.bankingserver.open.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.numble.bankingserver.open.domain.Account;
import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/*
 * 계좌 추가
 * 초기 잔고 입력 없이 계좌 추가
 * 계좌 추가시 유저 객체가 없으면 오류
 * 계좌 추가시 계좌번호가 없으면 오류
 * 계좌 추가시 계좌번호 중복되면 오류
 * 계좌 추가시 초기 잔고가 음수면 오류
 * existsAccountNumber가 잘 작동하는지
 * findByUserUserId가 잘 작동하는지
 * findByUserUserIdAndAccountNumber가 잘 작동하는지
 * */
@SpringBootTest
@Transactional
class AccountRepositoryTest {

    @Autowired
    AccountRepository repository;
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
    public void 계좌추가_성공() throws Exception {
        //given
        User user = User.builder().userId("userId").password("1234567890").name("User1").build();
        userRepository.save(user);

        Account account = Account.builder().user(user).accountNumber("1234-123-123456")
            .balance(1000).build();

        //when
        Account saveAccount = repository.save(account);

        //then
        //아직 예외 클래스를 만들지 않았기에 RuntimeException으로 처리
        Account findAccount = repository.findById(saveAccount.getId())
            .orElseThrow(() -> new RuntimeException("저장된 계좌가 없습니다."));
        assertThat(findAccount).isSameAs(saveAccount);
        assertThat(findAccount).isSameAs(account);
    }

    @Test
    public void 초기잔고입력_없이_계좌추가_성공() {
        //given
        User user = User.builder().userId("userId").password("1234567890").name("User1").build();
        userRepository.save(user);

        Account account = Account.builder().user(user).accountNumber("1234-123-123456").build();

        //when
        Account saveAccount = repository.save(account);

        //then
        //아직 예외 클래스를 만들지 않았기에 RuntimeException으로 처리
        Account findAccount = repository.findById(saveAccount.getId())
            .orElseThrow(() -> new RuntimeException("저장된 계좌가 없습니다."));
        assertThat(findAccount).isSameAs(saveAccount);
        assertThat(findAccount).isSameAs(account);
        assertThat(findAccount.getBalance()).isEqualTo(0);
    }

    @Test
    public void 오류_계좌추가시_유저가_없음() throws Exception {
        //given
        User user = User.builder().userId("userId").password("1234567890").name("User1").build();
        userRepository.save(user);

        Account account = Account.builder().accountNumber("1234-123-123456").balance(1000).build();

        //when, then
        assertThrows(Exception.class, () -> repository.save(account));
    }

    @Test
    public void 오류_계좌추가시_계좌번호가_없음() throws Exception {
        //given
        User user = User.builder().userId("userId").password("1234567890").name("User1").build();
        userRepository.save(user);

        Account account = Account.builder().user(user).balance(1000).build();

        //when, then
        assertThrows(Exception.class, () -> repository.save(account));
    }

    @Test
    public void 오류_계좌추가시_중복된_계좌번호_있음() throws Exception {
        //given
        User user1 = User.builder().userId("userId1").password("1234567890").name("User1").build();
        User user2 = User.builder().userId("userId2").password("1111111111").name("User2").build();

        userRepository.save(user1);
        userRepository.save(user2);

        Account account1 = Account.builder().user(user1).accountNumber("1234-123-123456")
            .balance(1000).build();
        Account account2 = Account.builder().user(user2).accountNumber("1234-123-123456")
            .balance(10000).build();

        repository.save(account1);
        clear();

        //when, then
        assertThrows(Exception.class, () -> repository.save(account2));
    }

    @Test
    public void 오류_계좌추가시_초기잔고가_음수() throws Exception {
        //given
        User user = User.builder().userId("userId").password("1234567890").name("User1").build();
        userRepository.save(user);

        Account account = Account.builder().user(user).accountNumber("1234-123-123456")
            .balance(-1000).build();

        //when, then
        assertThatThrownBy(() -> repository.save(account))
            .hasMessage("잔액은 음수일 수 없습니다.");
    }

    @Test
    public void existsAccountNumber_정상작동() throws Exception {
        //given
        String accountNumber = "1234-123-123456";
        String diffAccountNumber = "1234-123-123457";
        User user1 = User.builder().userId("userId").password("1234567890").name("User1").build();
        userRepository.save(user1);
        Account account = Account.builder()
            .user(user1)
            .accountNumber("1234-123-123456")
            .balance(1000)
            .build();
        repository.save(account);
        clear();

        //when, then
        assertThat(repository.existsByAccountNumber(accountNumber)).isTrue();
        assertThat(repository.existsByAccountNumber(diffAccountNumber)).isFalse();
    }

    @Test
    public void findByUserUserId_정상작동() throws Exception {
        //given
        User user1 = User.builder().userId("userId").password("1234567890").name("User1").build();
        userRepository.save(user1);
        Account account1 = Account.builder()
            .user(user1)
            .accountNumber("1234-123-123456")
            .balance(1000)
            .build();
        Account account2 = Account.builder()
            .user(user1)
            .accountNumber("1234-123-123457")
            .balance(1000)
            .build();
        repository.save(account1);
        repository.save(account2);
        clear();

        //when
        List<Account> findAccountList1 = repository.findByUserUserId("userId");

        //then
        assertThat(findAccountList1.size()).isEqualTo(2);
        assertThat(findAccountList1.get(0)).isEqualTo(account1);
        assertThat(findAccountList1.get(1)).isEqualTo(account2);

        //when
        List<Account> findAccountList2 = repository.findByUserUserId("userId1");

        //then
        assertThat(findAccountList2.size()).isEqualTo(0);

    }

    @Test
    public void findByUserUserIdAndAccountNumber_정상작동() throws Exception {
        //given
        User user1 = User.builder().userId("userId").password("1234567890").name("User1").build();
        userRepository.save(user1);
        Account account = Account.builder()
            .user(user1)
            .accountNumber("1234-123-123456")
            .balance(1000)
            .build();
        repository.save(account);
        clear();

        //when, then
        assertThat(
            repository.findByUserUserIdAndAccountNumber("userId", "1234-123-123456"))
            .isEqualTo(Optional.of(account));
        assertThat(
            repository.findByUserUserIdAndAccountNumber("userId", "1234-123-123457"))
            .isSameAs(Optional.empty());
    }
}