package com.companyName.projectName.model;

import java.util.List;
import lombok.Data;

@Data
public class BatchDeleteRequest {
  private List<String> ids = List.of();

}
