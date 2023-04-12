package com.companyName.projectName.controller;

import com.companyName.projectName.exception.OperateAbsentItemsException;
import com.companyName.projectName.model.BatchDeleteRequest;
import com.companyName.projectName.model.User;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

  private final Map<String, User> userDB = new LinkedHashMap<>();
  @PostConstruct
  private void initData() {
    var users = List.of(
        User.of("U1", "Vincent Chang", "vincent@gmail.com"),
        User.of("U2", "Ivy Chang", "ivy@gmail.com"),
        User.of("U3", "Dora Pan", "dora@gmail.com")
    );
    users.forEach(x -> userDB.put(x.getId(), x));
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteUsers(@RequestBody BatchDeleteRequest request) {
    var itemIds = request.getIds();
    var absentIds = itemIds.stream()
        .filter(Predicate.not(userDB::containsKey))
        .collect(Collectors.toList());
    if (!absentIds.isEmpty()) {
      throw new OperateAbsentItemsException(absentIds);
    }

    itemIds.forEach(userDB::remove);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/all")
  public ResponseEntity<List<User>> getUsers(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String email) {
    var stream = userDB.values().stream();
    if (name != null) {
      stream = stream.filter(u -> u.getName().toLowerCase().contains(name));
    }
    if (email != null) {
      stream = stream.filter(u -> u.getEmail().equalsIgnoreCase(email));
    }

    var users = stream.collect(Collectors.toList());
    return ResponseEntity.ok(users);
  }


}
