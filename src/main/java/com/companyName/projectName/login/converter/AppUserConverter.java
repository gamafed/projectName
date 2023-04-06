package com.companyName.projectName.login.converter;

import com.companyName.projectName.login.entity.AppUser;
import com.companyName.projectName.login.entity.AppUserRequest;
import com.companyName.projectName.login.entity.AppUserResponse;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class AppUserConverter {

    public static AppUser toAppUser(AppUserRequest request) {
        AppUser user = new AppUser();
        user.setEmailAddress(request.getEmailAddress());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setAuthorities(request.getAuthorities());

        return user;
    }

    public static AppUserResponse toAppUserResponse(AppUser user) {
        AppUserResponse response = new AppUserResponse();
        response.setId(user.getId());
        response.setEmailAddress(user.getEmailAddress());
        response.setName(user.getName());
        response.setAuthorities(user.getAuthorities());

        return response;
    }

    public static List<AppUserResponse> toAppUserResponses(List<AppUser> users) {
        return users.stream()
                .map(AppUserConverter::toAppUserResponse)
                .collect(Collectors.toList());
    }
}
