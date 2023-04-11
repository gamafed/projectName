package com.companyName.projectName.login.entity;

import com.companyName.projectName.login.auth.UserAuthority;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
@Data
public class AppUserRequest {

    @Schema(description = "The email address of user.", example = "XXXxxx@gmail.com")
    @NotBlank
    private String emailAddress;
    @Schema(description = "The password of user.", example = "123456", minLength = 6)
    @NotBlank
    private String password;
    @Schema(description = "The full name of user.", example = "Peter Zheng")
    @NotBlank
    private String name;
    @Schema(description = "The authority of user.", requiredMode= RequiredMode.REQUIRED)
    @NotEmpty
    private List<UserAuthority> authorities;
}
