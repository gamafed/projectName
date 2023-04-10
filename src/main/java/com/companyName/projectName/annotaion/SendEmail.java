package com.companyName.projectName.annotaion;

import com.companyName.projectName.enums.ActionType;
import com.companyName.projectName.enums.EntityType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SendEmail {
  EntityType entity();
  ActionType action();
  int idParamIndex() default -1;
}
