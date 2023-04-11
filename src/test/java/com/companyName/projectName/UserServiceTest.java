package com.companyName.projectName;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.companyName.projectName.exception.NotFoundException;
import com.companyName.projectName.login.entity.AppUser;
import com.companyName.projectName.login.service.AppUserService;
import com.companyName.projectName.login.service.SpringUserService;
import com.companyName.projectName.login.user.SpringUser;
import com.companyName.projectName.login.user.UserDetails;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// @ExtendWith annotation instead of @RunWith
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @Mock // 被依賴的元件
  private AppUserService appUserService;

  @InjectMocks // 待測元件
  private SpringUserService springUserService;

  @Test
  public void testLoadSpringUser() {
    String email = "vincent@gmail.com";
    AppUser appUser = new AppUser();
    appUser.setId("123");
    appUser.setEmailAddress(email);
    appUser.setName("Vincent Zheng");

    // 模擬appUserService.getUserByEmail(email) 返回appUser物件
    when(appUserService.getUserByEmail(email)).thenReturn(appUser);

    SpringUser springUser = (SpringUser) springUserService.loadUserByUsername(email);

    Assertions.assertEquals(appUser.getId(), springUser.getId());
    Assertions.assertEquals(appUser.getName(), springUser.getName());
    Assertions.assertEquals(appUser.getEmailAddress(), springUser.getUsername());
  }

  @Test
  public void testLoadSpringUserButNotFound() {
    // 模擬設定 appUserService.getUserByEmail(anyString()) 返回 NotFoundException("hahaha") 物件
    // 代表傳入任何字串，其行為都相同。
    when(appUserService.getUserByEmail(anyString())).thenThrow(new NotFoundException("hahaha"));
    assertThrows(
        UsernameNotFoundException.class,
        () -> {
          // 實際呼叫 loadUserByUsername 方法，確認有拋出預期的例外
          springUserService.loadUserByUsername("vincent@gmail.com");
        });
  }

  @Test
  public void testSetAuthoritiesAsNull() {
    AppUser user = mock(AppUser.class); // mock object
    // 模擬設定 authorities 屬性，當給予 null 值或空 List 時要拋出 IllegalArgumentException
    doThrow(new IllegalArgumentException()).when(user).setAuthorities(isNull());
    assertThrows(IllegalArgumentException.class, () -> {
      user.setAuthorities(null);
    });
  }

  @Test
  public void testSetAuthoritiesAsEmpty() {
    //setting
    AppUser user = mock(AppUser.class);
    //使用 doThrow 方法，傳入要拋出的例外物件
    //接著使用 when 方法，傳入模擬物件
    doThrow(new IllegalArgumentException())
        .when(user).setAuthorities(Collections.emptyList());

    assertThrows(IllegalArgumentException.class, () -> {
      //呼叫要模擬的方法
      user.setAuthorities(new ArrayList<>());
    });
  }
}
