package com.companyName.projectName.model;

import com.companyName.projectName.util.CommonUtil;
import java.util.Date;
import lombok.Data;

@Data
public class Product {
  private String id;
  private String name;
  private Date createdTime;

  public static Product of(String id, String name, String createdTime) {
    var product = new Product();
    product.id = id;
    product.name = name;
    product.createdTime = CommonUtil.toDate(createdTime);
    return product;
  }

}
