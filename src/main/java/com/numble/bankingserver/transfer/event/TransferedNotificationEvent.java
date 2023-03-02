package com.numble.bankingserver.transfer.event;

import com.numble.bankingserver.alarm.vo.AlarmMessage;
import com.numble.bankingserver.transfer.vo.TransferVO;
import lombok.Getter;

@Getter
public class TransferedNotificationEvent {

    private AlarmMessage fromUserMessage;
    private AlarmMessage toUserMessage;

    public TransferedNotificationEvent(TransferVO transfer) {
        String message = "[출금] : " + transfer.getToUser() + "님에게 송금이 완료되었습니다.";

        fromUserMessage = new AlarmMessage(transfer.getFromUser(), message);

        message =
            "[입금] : " + transfer.getFromUser() + "님이 " + transfer.getToAccountNumber() + " 계좌에 "
                + transfer.getAmount() + "원을 보냈습니다.";

        toUserMessage = new AlarmMessage(transfer.getToUser(), message);
    }
}
