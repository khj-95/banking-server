package com.numble.bankingserver.transfer.service;

import com.numble.bankingserver.follow.repository.FollowRepository;
import com.numble.bankingserver.open.domain.Account;
import com.numble.bankingserver.open.repository.AccountRepository;
import com.numble.bankingserver.transfer.event.TransferedNotificationEvent;
import com.numble.bankingserver.transfer.vo.TransferVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransferService {

    private final String TRANSFER_ERROR = "TRANSFER ERROR";
    private final String USER = "유저";
    private final String FRIEND = "친구";
    private final FollowRepository followRepository;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher publisher;

    public void transfer(TransferVO transfer) {
        try {
            checkFriendship(transfer);

            Account fromUserAccount =
                findAccount(transfer.getFromUser(), transfer.getFromAccountNumber(), USER);

            Account toUserAccount =
                findAccount(transfer.getToUser(), transfer.getToAccountNumber(), FRIEND);

            fromUserAccount.withdraw(transfer.getAmount());

            toUserAccount.deposit(transfer.getAmount());

            publisher.publishEvent(new TransferedNotificationEvent(transfer));

        } catch (RuntimeException e) {

            log.error(TRANSFER_ERROR);
            throw e;
        }
    }

    private void checkFriendship(TransferVO transfer) {
        String user = transfer.getFromUser();
        String friend = transfer.getToUser();

        if (followRepository.existsByFromUserUserIdAndToUserUserId(user, friend)
            && followRepository.existsByFromUserUserIdAndToUserUserId(friend, user)) {
            return;
        }

        throw new RuntimeException("친구끼리만 계좌 이체가 가능합니다.");
    }

    private Account findAccount(String userId, String accountNumber, String role) {
        return accountRepository.findByUserUserIdAndAccountNumber(userId, accountNumber)
            .orElseThrow(() -> {
                throw new RuntimeException("유효하지 않은 " + role + "의 계좌 정보입니다.");
            });
    }
}
