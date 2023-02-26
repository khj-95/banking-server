package com.numble.bankingserver.follow.vo;

import com.numble.bankingserver.follow.domain.Follow;
import com.numble.bankingserver.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class FollowVO {

    private String fromUserId;
    private String fromUserName;
    private String toUserId;
    private String toUserName;

    @Builder
    private FollowVO(String fromUserId, String fromUserName, String toUserId, String toUserName) {
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.toUserId = toUserId;
        this.toUserName = toUserName;
    }

    public static FollowVO createFollowVO(
        String fromUserId, String fromUserName, String toUserId, String toUserName) {
        return FollowVO.builder()
            .fromUserId(fromUserId)
            .fromUserName(fromUserName)
            .toUserId(toUserId)
            .toUserName(toUserName)
            .build();
    }

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FollowVO) {
            FollowVO tmp = (FollowVO) obj;
            return fromUserId.equals(tmp.getFromUserId())
                && fromUserName.equals(tmp.getFromUserName())
                && toUserId.equals(tmp.getToUserId())
                && toUserName.equals(tmp.getToUserName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromUserId, fromUserName, toUserId, toUserName);
    }
}
