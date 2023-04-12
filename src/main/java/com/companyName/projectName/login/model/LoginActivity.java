package com.companyName.projectName.login.model;

import java.util.Date;
import lombok.Data;

@Data
public class LoginActivity {
  private String name;
  private Date loginTime;
  private boolean notified = false;

  public static LoginActivity of(String name, Date loginTime) {
    var activity = new LoginActivity();
    activity.name = name;
    activity.loginTime = loginTime;
    return activity;
  }
}
