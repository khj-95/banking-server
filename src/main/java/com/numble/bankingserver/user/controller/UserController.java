package com.numble.bankingserver.user.controller;

import com.numble.bankingserver.user.dto.JoinDTO;
import com.numble.bankingserver.user.service.FollowService;
import com.numble.bankingserver.user.service.UserService;
import com.numble.bankingserver.user.vo.FollowVO;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/follow-list/{page}")
    public ResponseEntity<Page<FollowVO>> lookUpFollowList(@PathVariable("page") int page,
        Principal principal) {
        String userId = principal.getName();
        Page<FollowVO> followList = followService.lookUpFollowList(page, userId);
        return new ResponseEntity<>(followList, HttpStatus.OK);
    }
}
