package com.companyName.projectName.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) //422
public class OperateAbsentItemsException extends RuntimeException {
  private final List<String> itemIds;

  public OperateAbsentItemsException(List<String> itemIds) {
    super("Following ids are non-existent: " + itemIds.toString());
    this.itemIds = itemIds;
  }

  public List<String> getItemIds() {
    return itemIds;
  }
}
