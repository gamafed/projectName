package com.companyName.projectName.login.service;

import com.companyName.projectName.exception.NotFoundException;
import com.companyName.projectName.login.entity.AppUser;
// import com.companyName.projectName.login.user.UserDetails;
import com.companyName.projectName.login.user.SpringUser;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SpringUserService implements UserDetailsService {

  @Autowired private AppUserService appUserService;

  //    @Override
  //    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  //        AppUser appUser = appUserService.getUserByEmail(username);
  //
  //        List<SimpleGrantedAuthority> authorities = appUser.getAuthorities().stream()
  //                .map(auth -> new SimpleGrantedAuthority(auth.name()))
  //                .collect(Collectors.toList());
  //
  //        log.info("appUser= "+appUser);
  //        return new User(appUser.getEmailAddress(), appUser.getPassword(), authorities);
  //    }
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      AppUser appUser = appUserService.getUserByEmail(username);
      return new SpringUser(appUser);
    } catch (NotFoundException e) {
      throw new UsernameNotFoundException("Username is wrong.");
    }
  }
}
