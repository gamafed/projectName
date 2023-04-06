package com.companyName.projectName.login.entity;

import com.companyName.projectName.login.auth.UserAuthority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("users")
public class AppUser {
    private String id;
    private String emailAddress;
    private String password;
    private String name;
    private List<UserAuthority> authorities;

}
