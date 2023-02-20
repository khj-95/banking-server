package com.numble.bankingserver.user.controller;

import com.numble.bankingserver.user.dto.JoinDTO;
import com.numble.bankingserver.user.service.FollowService;
import com.numble.bankingserver.user.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final FollowService followService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinDTO joinDTO) throws Exception {
        service.joinUser(joinDTO);
        return new ResponseEntity<>("회원가입 완료", HttpStatus.OK);
    }

    @PostMapping("/follow/{following-id}")
    public ResponseEntity<String> follow(@PathVariable("following-id") String followingId,
        Principal principal) throws Exception {
        String userId = principal.getName();

        followService.follow(followingId, userId);

        return new ResponseEntity<>("친구 추가 완료", HttpStatus.OK);
    }
}
