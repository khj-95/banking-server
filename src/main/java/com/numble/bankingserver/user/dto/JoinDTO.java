package com.numble.bankingserver.user.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {

    @NotNull
    private String id;
    private String password;
    private String name;
}
