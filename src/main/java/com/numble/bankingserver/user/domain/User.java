package com.numble.bankingserver.user.domain;

import com.numble.bankingserver.user.dto.JoinDTO;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;


/*
 * 기본 생성자(NoArgsConstructor)의 접근 제어를 PROCTECTED 로 설정하면
 * 아무런 값도 갖지 않는 의미 없는 객체의 생성을 막을 수 있다.
 */
@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userId;
    private String password;
    private String name;

    @Builder
    private User(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

    public static User createUser(JoinDTO joinDTO) {
        return User.builder()
            .userId(joinDTO.getUserId())
            .password(joinDTO.getPassword())
            .name(joinDTO.getName())
            .build();
    }

    // 비밀번호 암호화 메서드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
