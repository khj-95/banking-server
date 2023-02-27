package com.numble.bankingserver.user.controller;

import com.numble.bankingserver.user.exception.UserAlreadyExistsException;
import com.numble.bankingserver.user.service.UserService;
import com.numble.bankingserver.user.vo.JoinVO;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1. 회원가입 API 2. 친구 추가 API 3. 내 친구 목록 조회 API
 */

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody JoinVO joinVO) {
        HttpStatus status;
        try {
            service.joinUser(joinVO);
            status = HttpStatus.CREATED;
        } catch (UserAlreadyExistsException exception) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>("회원가입 완료", status);
    }
}
