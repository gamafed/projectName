package com.companyName.projectName.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ExceptionResponse {
  private BusinessExceptionType type;
  private Map<String, Object> info = new HashMap<>();

  public static ExceptionResponse of(BusinessExceptionType type, Map<String, Object> info) {
    var res = new ExceptionResponse();
    res.type = type;
    res.info = info;

    return res;
  }
}
