package com.numble.bankingserver.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {

    private String userId;
    private String password;
    private String name;
}
