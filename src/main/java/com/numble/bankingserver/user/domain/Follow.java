package com.numble.bankingserver.user.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
    name = "follow",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"from_user_id", "to_user_id"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User fromUser;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User toUser;

    @Builder
    private Follow(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public static Follow createFollow(User fromUser, User toUser) {
        return Follow.builder()
            .fromUser(fromUser)
            .toUser(toUser)
            .build();
    }

}
