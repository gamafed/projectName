package com.companyName.projectName.login.entity;

import com.companyName.projectName.login.auth.UserAuthority;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
@Data
public class AppUserRequest {
    @NotBlank
    private String emailAddress;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotEmpty
    private List<UserAuthority> authorities;
}
