package com.numble.bankingserver.user.controller;

import com.numble.bankingserver.user.dto.JoinDTO;
import com.numble.bankingserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1. 회원가입 API 2. 로그인 API 3. 친구 추가 API 4. 내 친구 목록 조회 API
 */

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinDTO joinDTO) {
        service.joinUser(joinDTO);
        return new ResponseEntity<>("회원가입 완료", HttpStatus.OK);
    }

}
