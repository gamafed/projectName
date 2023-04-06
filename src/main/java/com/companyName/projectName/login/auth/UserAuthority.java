package com.companyName.projectName.login.auth;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum UserAuthority {
    ADMIN, NORMAL;

    @JsonCreator
    public UserAuthority fromString(String key) {
        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(key))
                .findFirst()
                .orElse(null);
    }

}
