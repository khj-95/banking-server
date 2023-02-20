package com.numble.bankingserver.user.vo;

import com.numble.bankingserver.user.domain.Follow;
import com.numble.bankingserver.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@Builder
public class FollowVO {

    private String fromUserId;
    private String fromUserName;
    private String toUserId;
    private String toUserName;

    public static List<FollowVO> toFollowList(Page<Follow> followList) {
        List<FollowVO> followVOList = new ArrayList<>();
        for (Follow follow : followList) {
            User fromUser = follow.getFromUser();
            User toUser = follow.getToUser();
            FollowVO vo = FollowVO.builder()
                .fromUserId(fromUser.getUserId())
                .fromUserName(fromUser.getName())
                .toUserId(toUser.getUserId())
                .toUserName(toUser.getName())
                .build();

            followVOList.add(vo);
        }
        return followVOList;
    }
}
