package com.numble.bankingserver.follow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.numble.bankingserver.follow.domain.Follow;
import com.numble.bankingserver.follow.repository.FollowRepository;
import com.numble.bankingserver.follow.vo.FollowVO;
import com.numble.bankingserver.user.domain.User;
import com.numble.bankingserver.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    FollowRepository followRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    FollowService followService;

    @Test
    public void follow가_정상작동() {
        User fromUser = User.builder()
            .userId("userId1")
            .password("1234567890")
            .name("User1")
            .build();
        User toUser = User.builder()
            .userId("userId2")
            .password("1234567890")
            .name("User2").build();

        when(userRepository.findByUserId(any()))
            .thenReturn(Optional.of(fromUser), Optional.of(toUser));
        when(followRepository.existsByFromUserAndToUser(any(), any())).thenReturn(false);

        assertDoesNotThrow(() -> followService.follow("userId1", "userId2"));
    }

    @Test
    public void 오류_요청자_정보가_유효하지_않음() {
        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> followService.follow("userId1", "userId2"))
            .hasMessage("요청자 정보가 유효하지 않습니다.");
    }

    @Test
    public void 오류_추가하려는_친구_정보가_유효하지_않음() {
        User fromUser = User.builder().userId("userId1").password("1234567890").name("User1")
            .build();
        when(userRepository.findByUserId(any()))
            .thenReturn(Optional.of(fromUser), Optional.empty());

        assertThatThrownBy(() -> followService.follow("userId1", "userId2"))
            .hasMessage("추가할 친구 정보가 유효하지 않습니다.");
    }

    @Test
    public void 오류_추가하려는_친구정보와_유저정보가_일치함() {
        User fromUser = User.builder().userId("userId1").password("1234567890").name("User1")
            .build();
        User toUser = User.builder().userId("userId1").password("1234567890").name("User1")
            .build();

        when(userRepository.findByUserId(any()))
            .thenReturn(Optional.of(fromUser), Optional.of(toUser));

        assertThatThrownBy(() -> followService.follow("userId1", "userId1"))
            .hasMessage("유효하지 않은 친구 추가 요청입니다.");
    }

    @Test
    public void 오류_친구목록에_요청하려는_친구정보가_존재함() {
        User fromUser = User.builder().userId("userId1").password("1234567890").name("User1")
            .build();
        User toUser = User.builder().userId("userId2").password("1234567890").name("User2")
            .build();

        when(userRepository.findByUserId(any()))
            .thenReturn(Optional.of(fromUser), Optional.of(toUser));
        when(followRepository.existsByFromUserAndToUser(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> followService.follow("userId1", "userId1"))
            .hasMessage("이미 존재하는 친구입니다.");
    }

    @Test
    public void lookUpFollowList가_정상작동() {
        User user1 = User.builder()
            .userId("userId1")
            .password("1234567890")
            .name("User1")
            .build();

        List<Follow> followList = new ArrayList<>();
        List<FollowVO> followVOList = new ArrayList<>();

        for (int i = 2; i <= 105; i++) {
            User tmp = User.builder()
                .userId("userId" + i)
                .password("1234567890")
                .name("User" + i)
                .build();

            followList.add(Follow.createFollow(user1, tmp));
            followVOList.add(
                FollowVO.createFollowVO("userId1", "User1", "userId" + i, "User" + i)
            );
        }

        int page = 1;
        PageRequest pageRequest = PageRequest.of(page, 20);
        Page<Follow> followPage = new PageImpl<>(followList, pageRequest, 100);

        when(followRepository.findByFromUserUserId(any(), any())).thenReturn(followPage);

        assertThat(followService.lookUpFollowList(page, "userId1").getTotalElements())
            .isEqualTo(100);
        assertThat(followService.lookUpFollowList(page, "userId1").getTotalPages())
            .isEqualTo(5);
        assertThat(followService.lookUpFollowList(page, "userId1").getContent())
            .usingRecursiveComparison()
            .isEqualTo(followVOList);
    }

    @Test
    public void 오류_페이지값이_음수() {
        assertThatThrownBy(() -> followService.lookUpFollowList(-1, "userId1"))
            .hasMessage("잘못된 페이지 요청입니다. 0 이상의 정수를 입력해주세요.");
    }

    @Test
    public void 오류_마지막_페이지_초과() {
        int page = 6;
        PageRequest pageRequest = PageRequest.of(page, 20);
        Page<Follow> followPage = new PageImpl<>(new ArrayList<>(), pageRequest, 104);

        when(followRepository.findByFromUserUserId(any(), any())).thenReturn(followPage);

        assertThatThrownBy(() -> followService.lookUpFollowList(page, "userId1"))
            .hasMessageContaining("잘못된 페이지 요청입니다.(" + (followPage.getTotalPages() - 1) + "이하의 정수");
    }
}