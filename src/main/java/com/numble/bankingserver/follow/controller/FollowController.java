package com.numble.bankingserver.follow.controller;

import com.numble.bankingserver.follow.service.FollowService;
import com.numble.bankingserver.follow.vo.FollowVO;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

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
