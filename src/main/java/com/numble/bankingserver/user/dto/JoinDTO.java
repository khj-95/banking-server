package com.numble.bankingserver.user.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {

    @NotBlank
    private String id;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
}
