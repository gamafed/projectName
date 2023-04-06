package com.companyName.projectName.login.entity;

import com.companyName.projectName.login.auth.UserAuthority;
import lombok.Data;

import java.util.List;

@Data
public class AppUserResponse {
    private String id;
    private String emailAddress;
    private String name;
    private List<UserAuthority> authorities;
}
