package com.companyName.projectName.login.auth;

import com.companyName.projectName.login.entity.AppUser;
import com.companyName.projectName.login.user.SpringUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserIdentity {

  private final SpringUser EMPTY_USER = new SpringUser(new AppUser());

  private SpringUser getSpringUser() {
    // Security 的「Context」內有 authentication
    // ThreadLocal by Context
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    return principal.equals("anonymousUser")
        ? EMPTY_USER
        : (SpringUser) principal;
  }

  public boolean isAnonymous() {
    return EMPTY_USER.equals(getSpringUser());
  }

  public String getId() {
    return getSpringUser().getId();
  }

  public String getName() {
    return getSpringUser().getName();
  }

  public String getEmail() {
    return getSpringUser().getUsername();
  }

}
