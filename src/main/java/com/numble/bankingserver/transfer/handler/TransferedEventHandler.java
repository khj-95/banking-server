package com.numble.bankingserver.transfer.handler;

import com.numble.bankingserver.transfer.event.TransferedNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Slf4j
public class TransferedEventHandler {

    @TransactionalEventListener
    public void notifyTransfer(TransferedNotificationEvent event) throws InterruptedException {
        Thread.sleep(500);
        log.info("알람 전송 완료");
    }
}
