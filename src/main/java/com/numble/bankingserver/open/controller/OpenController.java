package com.numble.bankingserver.open.controller;

import com.numble.bankingserver.open.service.OpenService;
import com.numble.bankingserver.open.vo.OpenAccountVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenController {

    private final OpenService openService;

    @PostMapping("/open-account")
    public ResponseEntity<String> openAccount(@RequestBody OpenAccountVO openAccount) {
        openService.openAccount(openAccount);
        return new ResponseEntity<>("계좌 개설 완료", HttpStatus.OK);
    }
}
