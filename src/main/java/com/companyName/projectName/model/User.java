package com.companyName.projectName.model;

import lombok.Data;

@Data
public class User {
  private String id;
  private String name;
  private String email;

  public static User of(String id, String name, String email) {
    var user = new User();
    user.id = id;
    user.name = name;
    user.email = email;
    return user;
  }

}
