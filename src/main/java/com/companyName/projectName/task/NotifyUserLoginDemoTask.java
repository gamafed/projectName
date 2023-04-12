package com.companyName.projectName.task;

import com.companyName.projectName.login.model.TaskCycleConfig;
import com.companyName.projectName.login.repository.LoginActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class NotifyUserLoginDemoTask implements SchedulingConfigurer{ //動態更改週期
  private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm:ss");
  private int count = 1;

  //藉由 TaskCycleConfig 全域變數，來決定新的週期
  private TaskCycleConfig config = TaskCycleConfig.of(15000);

  @Autowired
  private LoginActivityRepository repository;

//  initialDelay：後端程式啟動完成後，先延遲一段時間才開始第一次執行。
//  fixDelay：上一次任務結束後，間隔一段時間才執行下一次。
//  fixRate：每隔一段時間就開始一次新的任務。若任務執行時間大於此間隔，並不會造成多個任務同時執行。
//  cron：用 cron 表示式定義執行週期。

  @Scheduled(initialDelay = 20000, fixedDelay = 5000)
  public void createLoginData() {
    var name = "User_" + count;
    repository.insert(name);

    log.info("{} 登入", name);
    count++;
  }

//  @Scheduled(cron = "${task.notify_login_user.cron}")
  public void notifyLoginUser() {
    log.info("開始發送登入通知");

    var activities = repository.findByNotNotified();
    activities.forEach(act -> {
      var timeStr = sdf.format(act.getLoginTime());
      log.info("親愛的 {}，您於 {} 登入本服務。", act.getName(), timeStr);

      act.setNotified(true);
    });
  }

  public void setNotifyConfig(TaskCycleConfig config) {
    this.config = config;
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) { //ScheduledTaskRegistrar 物件，可以註冊任務
    //addFixedDelayTask、addFixedRateTask 與 addCronTask
    //addTriggerTask
    Runnable runnable = this::notifyLoginUser;
    System.out.println("TaskCycleConfig= "+config);

    //實作 nextExecutionTime
    Trigger trigger = triggerContext -> {
      if (config.getDelay() != null) {
        var t = new PeriodicTrigger(config.getDelay(), TimeUnit.MILLISECONDS);
        t.setFixedRate(false);
        return t.nextExecutionTime(triggerContext);

      } else if (config.getRate() != null) {
        var t = new PeriodicTrigger(config.getRate(), TimeUnit.MILLISECONDS);
        t.setFixedRate(true);
        return t.nextExecutionTime(triggerContext);

      } else if (config.getCron() != null) {
        return new CronTrigger(config.getCron()).nextExecutionTime(triggerContext);

      } else {
        throw new RuntimeException("Please define at least one schedule parameter.");

      }
    };

    taskRegistrar.addTriggerTask(runnable, trigger); // register trigger
  }
}