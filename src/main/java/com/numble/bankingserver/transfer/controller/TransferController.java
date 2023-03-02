package com.numble.bankingserver.transfer.controller;

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

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferVO transfer) {
        transferService.transfer(transfer);
        return new ResponseEntity<>("계좌 이체 완료", HttpStatus.OK);
    }
}
