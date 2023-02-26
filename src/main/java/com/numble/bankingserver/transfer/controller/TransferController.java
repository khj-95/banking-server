package com.numble.bankingserver.transfer.controller;

import com.numble.bankingserver.alarm.service.AlarmService;
import com.numble.bankingserver.transfer.service.TransferService;
import com.numble.bankingserver.transfer.vo.TransferVO;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final AlarmService alarmService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferVO transfer)
        throws InterruptedException {
        transferService.transfer(transfer);
        alarmService.notify(transfer.getFromUser(), "계좌 이체 완료");
        return new ResponseEntity<>("계좌 이체 완료", HttpStatus.OK);
    }
}
