package com.numble.bankingserver.open.repository;

import com.numble.bankingserver.open.domain.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByUserUserId(String userId);

    Optional<Account> findByUserUserIdAndAccountNumber(String userId, String accountNumber);

}
