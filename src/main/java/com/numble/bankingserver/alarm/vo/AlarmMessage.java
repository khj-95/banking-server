package com.numble.bankingserver.alarm.vo;

import com.fasterxml.jackson.annotation.JsonValue;

public class AlarmMessage {

    private String id;
    private String message;

    public AlarmMessage(String id, String message) {
        this.id = id;
        this.message = message;
    }

    @JsonValue
    public String getId() {
        return id;
    }

    @JsonValue
    public String getMessage() {
        return message;
    }
}
