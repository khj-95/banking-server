package com.numble.bankingserver.user.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinVO {

    @NotBlank
    private String id;
    @NotBlank
    private String password;
    @NotBlank
    private String name;

    @JsonCreator
    @Builder
    public JoinVO(@JsonProperty("id") String id, @JsonProperty("password") String password,
        @JsonProperty("name") String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }
}
