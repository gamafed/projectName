package com.companyName.projectName.login.model;

import lombok.Data;

@Data
public class TaskCycleConfig {
  private Integer delay;
  private Integer rate;
  private String cron;

  public static TaskCycleConfig of(int delay) {
    var config = new TaskCycleConfig();
    config.delay = delay;

    return config;
  }
}
