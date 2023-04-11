package com.companyName.projectName;

import com.companyName.projectName.JWT.AuthRequest;
import com.companyName.projectName.entity.Product;
import com.companyName.projectName.login.auth.UserAuthority;
import com.companyName.projectName.login.entity.AppUser;
import com.companyName.projectName.request.ProductRequest;
import com.companyName.projectName.response.ProductResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
public class RestTemplateTest extends BaseTest{
//  如果在测试中需要使用已经启动的 Web 服务器的端口号，就可以使用 @LocalServerPort 注解来获取随机端口号。
  @LocalServerPort
  private int port;
  private String domain;
  private RestTemplate restTemplate;

  @BeforeEach
  public void init() {
    this.domain = "http://localhost:" + port;
    this.restTemplate = new RestTemplate();
  }

  private Product createProduct(String name, int price) {
    Product product = new Product();
    product.setName(name);
    product.setPrice(price);

    return productRepository.insert(product);
  }

  @Test
  public void testGetProduct() {
    Product product = createProduct("Snack", 50);

    String url = domain + "/products/" + product.getId();
    ProductResponse productRes = restTemplate
        .getForObject(url, ProductResponse.class);
    //ForObject 將回應主體（response body）轉換為指定的類別

    System.out.println("productRes="+productRes);
    Assertions.assertNotNull(productRes);
    Assertions.assertEquals(product.getId(), productRes.getId());
    Assertions.assertEquals(product.getName(), productRes.getName());
    Assertions.assertEquals(product.getPrice(), productRes.getPrice());
  }

  @Test
  public void testGetProducts() {
    Product p1 = createProduct("Operation Management", 350);
    Product p2 = createProduct("Marketing Management", 200);
    Product p3 = createProduct("Financial Statement Analysis", 400);
    Product p4 = createProduct("Human Resource Management", 420);
    Product p5 = createProduct("Enterprise Resource Planning", 440);

    String url = domain + "/products?keyword={name}&orderBy={orderField}&sortRule={sortDirection}";
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("name", "manage");
    queryParams.put("orderField", "price");
    queryParams.put("sortDirection", "asc");

    ProductResponse[] productResList = restTemplate
        .getForObject(url, ProductResponse[].class, queryParams);

    Assertions.assertNotNull(productResList);
    Assertions.assertEquals(3, productResList.length);
    Assertions.assertEquals(p2.getId(), productResList[0].getId());
    Assertions.assertEquals(p1.getId(), productResList[1].getId());
    Assertions.assertEquals(p4.getId(), productResList[2].getId());
  }

  private String obtainAccessToken(String username) {
    AuthRequest authReq = new AuthRequest();
    authReq.setUsername(username);
    authReq.setPassword(USER_PASSWORD);

    String url = this.domain + "/auth";
    Map<?,?> tokenRes = this.restTemplate
        .postForObject(url, authReq, Map.class);

    Assertions.assertNotNull(tokenRes);
    String token = (String) tokenRes.get("token");
    Assertions.assertNotNull(token);
    System.out.println("token= "+token);
    return token;
  }

  @Test
  public void testUserAuthentication() {
    AppUser appUser = createUser("FedTestTtTt", Collections.singletonList(UserAuthority.NORMAL));
    String token = obtainAccessToken(appUser.getEmailAddress());
  }

  @Test
  public void testCreateProduct() {
    AppUser appUser = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));

    ProductRequest productReq = new ProductRequest();
    productReq.setName("Snack");
    productReq.setPrice(50);

    String token = obtainAccessToken(appUser.getEmailAddress());
    this.httpHeaders.add(HttpHeaders.AUTHORIZATION, token);

    // request body 的資料與 HttpHeaders 物件包裝成 HttpEntity
    HttpEntity<ProductRequest> httpEntity = new HttpEntity<>(productReq, this.httpHeaders);
    String url = this.domain + "/products";
    ProductResponse productRes = this.restTemplate
        .postForObject(url, httpEntity, ProductResponse.class);

    Assertions.assertNotNull(productRes);
    Assertions.assertEquals(productReq.getName(), productRes.getName());
    Assertions.assertEquals(productReq.getPrice().intValue(), productRes.getPrice());
    Assertions.assertEquals(appUser.getId(), productRes.getCreator());
  }

  @Test
  public void testReplaceProduct() {
    this.productRepository.deleteAll();
    this.appUserRepository.deleteAll();
    AppUser appUser = createUser("Vincent", Collections.singletonList(UserAuthority.NORMAL));
    Product product = createProduct("Snack", 50);

    ProductRequest productReq = new ProductRequest();
    productReq.setName("Fruit");
    productReq.setPrice(80);

    String token = obtainAccessToken(appUser.getEmailAddress());
    httpHeaders.add(HttpHeaders.AUTHORIZATION, token);

    HttpEntity<ProductRequest> httpEntity = new HttpEntity<>(productReq, httpHeaders);

    String url = domain + "/products/" + product.getId();
    //使用 exchange 方法來發送請求
    ResponseEntity<ProductResponse> resEntity = restTemplate
        .exchange(url, HttpMethod.PUT, httpEntity, ProductResponse.class);

    Assertions.assertEquals(HttpStatus.OK, resEntity.getStatusCode());

    ProductResponse productRes = resEntity.getBody();
    System.out.println("productRes= "+productRes);
    Assertions.assertNotNull(productRes);
    Assertions.assertEquals(productReq.getName(), productRes.getName());
    Assertions.assertEquals(productReq.getPrice().intValue(), productRes.getPrice());

    String url1 = domain + "/products";
    HttpEntity<Void> httpEntity1 = new HttpEntity<>(null, null);
    ResponseEntity<List<ProductResponse>> productResponses =
        restTemplate.exchange(url1, HttpMethod.GET, httpEntity1,
            new ParameterizedTypeReference<List<ProductResponse>>() {});
    //回傳陣列 將 response 轉成具有泛型類別的 List
    List<ProductResponse> p = productResponses.getBody();
    System.out.println("p= "+p);
  }

  @Test
  public void testExchangeRateAPI() {
    //將 response body 轉換為 Java 物件 設置支援的 Content-Type，即 text/html
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));

    this.restTemplate = new RestTemplateBuilder()
        .setConnectTimeout(Duration.ofSeconds(10))
        //RestTemplate 底層是透過 ObjectMapper 將 JSON 資料轉為 Java 物件
        .additionalMessageConverters(converter)
        .build();

    String url = "https://www.freeforexapi.com/api/live?pairs=USDTWD,USDEUR";
    HttpEntity<Void> httpEntity = new HttpEntity<>(null, null);
    ResponseEntity<ExchangeRateResponse> resEntity = restTemplate
        .exchange(url, HttpMethod.GET, httpEntity,
            new ParameterizedTypeReference<ExchangeRateResponse>() {});

    Assertions.assertEquals(HttpStatus.OK, resEntity.getStatusCode());

    ExchangeRateResponse exRateRes = resEntity.getBody();
    System.out.println("exRateRes= "+exRateRes);
    Assertions.assertNotNull(exRateRes);
    Assertions.assertNotNull(exRateRes.getRates().get("USDEUR"));
    Assertions.assertNotNull(exRateRes.getRates().get("USDTWD"));
  }

}


@Data
class ExchangeRateResponse {
  private Map<String, RateData> rates;
  private int code;
}

@Data
class RateData {
  private double rate;
  private long timestamp;
}
