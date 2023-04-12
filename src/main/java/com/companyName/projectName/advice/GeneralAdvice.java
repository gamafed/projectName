package com.companyName.projectName.advice;

import com.companyName.projectName.controller.ProductController;
import com.companyName.projectName.controller.UserController;
import com.companyName.projectName.exception.OperateAbsentItemsException;
import com.companyName.projectName.model.BusinessExceptionType;
import com.companyName.projectName.model.ExceptionResponse;
import com.companyName.projectName.util.CommonUtil;
import com.companyName.projectName.util.ToSearchTextEditor;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {ProductController.class, UserController.class})
public class GeneralAdvice {
//  assignableTypes：直接指定 controller。
//  basePackages：package 下的所有 controller。
//  basePackageClasses：controller 所隸屬的父 package，其下所有的 controller。

  //CustomDateEditor 物件，是一種 PropertyEditor
  private final CustomDateEditor customDateEditor = new CustomDateEditor(CommonUtil.sdf, true);
  private final ToSearchTextEditor toSearchTextEditor = new ToSearchTextEditor();
  @InitBinder({"createdFrom", "createdTo"}) //處理「createdFrom」與「createdTo」兩個 query params
  public void bindDate(WebDataBinder binder) {
    //註冊「PropertyEditor」物件
    binder.registerCustomEditor(Date.class, //將 query params 轉換成的類別
        customDateEditor);
  }
  @InitBinder({"name", "email"})
  public void bindSearchText(WebDataBinder binder) {
    binder.registerCustomEditor(String.class, toSearchTextEditor);
  }

  //例外處理特殊做法將 log 存資料庫 用 Advice
  @ExceptionHandler(OperateAbsentItemsException.class) //custom 422 resp msg
  public ResponseEntity<ExceptionResponse> handleOperateAbsentItem(OperateAbsentItemsException e) {
    Map<String, Object> info = Map.of("itemIds", e.getItemIds());
    var res = new ExceptionResponse();
    res.setType(BusinessExceptionType.OPERATE_ABSENT_ITEM);
    res.setInfo(info);

    return ResponseEntity.unprocessableEntity().body(res);
  }


}
