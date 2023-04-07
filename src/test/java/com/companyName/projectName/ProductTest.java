package com.companyName.projectName;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.HttpHeaders;
import com.companyName.projectName.JWT.AuthRequest;
import com.companyName.projectName.entity.Product;
import com.companyName.projectName.login.auth.UserAuthority;
import com.companyName.projectName.login.entity.AppUser;
import com.companyName.projectName.login.repository.AppUserRepository;
import com.companyName.projectName.repository.ProductRepository;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {
    @Autowired
    private MockMvc mockMvc;
    private HttpHeaders httpHeaders;
    private Product product = createProduct("Economics", 450);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    public void init() {
//        productRepository.deleteAll();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        productRepository.insert(product);
        System.out.println("BeforeEach done");
    }

    @AfterEach
    public void clear() {
        productRepository.deleteAll();
        System.out.println("AfterEach done");
    }

    private Product createProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }

    @Test
    @Order(2)
    public void testCreateProduct() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/" + product.getId())
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers
                        .print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.id")
                        .value(product.getId()))
                .andExpect(jsonPath("$.name")
                        .value(product.getName()))
                .andExpect(jsonPath("$.price")
                        .value(product.getPrice()));
        System.out.println("testCreateProduct done");
    }

    @Test
    @Order(1)
    public void testReplaceProduct() throws Exception {

        JSONObject request = new JSONObject()
                .put("name", "Macroeconomics")
                .put("price", 550);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/products/" + product.getId())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.price").value(request.getInt("price")));
        System.out.println("testReplaceProduct done");
    }

    @Test
    @Order(3)
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + product.getId())
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers
                        .print())
                .andExpect(status().isNoContent());

        Assertions.assertThrows(RuntimeException.class, () -> {
            productRepository
                    .findById(product.getId())
                    .orElseThrow(RuntimeException::new);
        });
        System.out.println("testDeleteProduct done");
    }

    @Test
    @Order(4)
    public void testSearchProductsSortByPriceAsc() throws Exception {
        Product p1 = createProduct("Operation Management", 350);
        Product p2 = createProduct("Marketing Management", 200);
        Product p3 = createProduct("Human Resource Management", 420);
        Product p4 = createProduct("Finance Management", 400);
        Product p5 = createProduct("Enterprise Resource Planning", 440);
        productRepository.insert(Arrays.asList(p1, p2, p3, p4, p5));

        /*mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .headers(httpHeaders)
                        .param("keyword", "Manage")
                        .param("orderBy", "price")
                        .param("sortRule", "asc"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(p2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(p1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(p4.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].id").value(p3.getId()));*/

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .headers(httpHeaders)
                        .param("keyword", "Manage")
                        .param("orderBy", "price")
                        .param("sortRule", "asc"))
                .andReturn();

        MockHttpServletResponse mockHttpResponse = result.getResponse();

        String responseJSONStr = mockHttpResponse.getContentAsString();
        JSONArray productJSONArray = new JSONArray(responseJSONStr);

        List<String> productIds = new ArrayList<>();
        for (int i = 0; i < productJSONArray.length(); i++) {
            JSONObject productJSON = productJSONArray.getJSONObject(i);
            productIds.add(productJSON.getString("id"));
        }

        /*junit 5 use*/
        Assertions.assertEquals(4, productIds.size());
        Assertions.assertAll("check id of order",
                () -> Assertions.assertEquals(p2.getId(), productIds.get(0),"1st should be p2"),
                () -> Assertions.assertEquals(p1.getId(), productIds.get(1),"2nd should be p1"),
                () -> Assertions.assertEquals(p4.getId(), productIds.get(2),"3rd should be p4"),
                () -> Assertions.assertEquals(p3.getId(), productIds.get(3),"4th should be p3")
        );
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpResponse.getStatus());
        Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE,
                mockHttpResponse.getHeader(HttpHeaders.CONTENT_TYPE));

        System.out.println("testSearchProductsSortByPriceAsc done");
    }

    @Test
    public void get400WhenCreateProductWithEmptyName() throws Exception {
        JSONObject request = new JSONObject()
                .put("name", "")
                .put("price", 350);

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get400WhenReplaceProductWithNegativePrice() throws Exception {
        Product product = createProduct("Computer Science", 350);
        productRepository.insert(product);

        JSONObject request = new JSONObject()
                .put("name", "Computer Science")
                .put("price", -100)
                ;

        mockMvc.perform(MockMvcRequestBuilders.put("/products/" + product.getId())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isBadRequest());
    }
}
