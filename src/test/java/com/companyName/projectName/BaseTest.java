package com.companyName.projectName;

import com.companyName.projectName.JWT.AuthRequest;
import com.companyName.projectName.login.auth.UserAuthority;
import com.companyName.projectName.login.entity.AppUser;
import com.companyName.projectName.login.repository.AppUserRepository;
import com.companyName.projectName.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class BaseTest {

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ProductRepository productRepository;

  @Autowired protected AppUserRepository appUserRepository;
  protected final ObjectMapper mapper = new ObjectMapper();
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final String USER_PASSWORD = "123456";
  protected HttpHeaders httpHeaders;

  @BeforeEach
  public void initHttpHeader() {
    httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
  }

  @AfterEach
  public void clearDB() {
    productRepository.deleteAll();
    appUserRepository.deleteAll();
  }

  protected AppUser createUser(String name, List<UserAuthority> authorities) {
    AppUser appUser = new AppUser();
    appUser.setEmailAddress(name + "@test.com");
    appUser.setPassword(passwordEncoder.encode(USER_PASSWORD));
    appUser.setName(name);
    appUser.setAuthorities(authorities);

    return appUserRepository.insert(appUser);
  }

  protected void login(String emailAddress) throws Exception {
    AuthRequest authReq = new AuthRequest();
    authReq.setUsername(emailAddress);
    authReq.setPassword(USER_PASSWORD);
    MvcResult result =
        mockMvc
            .perform(
                post("/auth") // 將帳號與明文密碼發送到「POST /auth」的 API
                    .headers(httpHeaders)
                    .content(mapper.writeValueAsString(authReq)))
            .andExpect(status().isOk())
            .andReturn();

    JSONObject tokenRes = new JSONObject(result.getResponse().getContentAsString());
    String accessToken = tokenRes.getString("token");
    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
  }

  protected void logout() {
    httpHeaders.remove(HttpHeaders.AUTHORIZATION);
  }

  @Test
  protected void testAll() throws Exception {
    AppUser appUser = createUser("fedtest", Arrays.asList(UserAuthority.ADMIN, UserAuthority.NORMAL));
    login(appUser.getEmailAddress());

    // 發送 MockMVC 請求
    JSONObject productReq = new JSONObject();
    productReq.put("name", "Harry Potter");
    productReq.put("price", 450);
    mockMvc.perform(post("/products")
            .headers(httpHeaders)
            .content(productReq.toString()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").hasJsonPath())
        .andExpect(jsonPath("$.name").value(productReq.getString("name")))
        .andExpect(jsonPath("$.price").value(productReq.getInt("price")))
        .andExpect(jsonPath("$.creator").value(appUser.getId()));
    logout();
  }
}
