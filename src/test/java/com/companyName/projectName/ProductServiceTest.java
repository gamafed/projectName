package com.companyName.projectName;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.companyName.projectName.converter.ProductConverter;
import com.companyName.projectName.entity.Product;
import com.companyName.projectName.login.auth.UserIdentity;
import com.companyName.projectName.repository.ProductRepository;
import com.companyName.projectName.request.ProductRequest;
import com.companyName.projectName.response.ProductResponse;
import com.companyName.projectName.service.ProductService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;


public class ProductServiceTest {

  @Test
  public void testConvertProductToResponse() {
    Product product = new Product();
    product.setId("123");
    product.setName("Snack");
    product.setPrice(50);
    product.setCreator("abc");
    ProductResponse productResponse = ProductConverter.toProductResponse(product);

    Assertions.assertEquals(product.getId(), productResponse.getId());
    Assertions.assertEquals(product.getName(), productResponse.getName());
    Assertions.assertEquals(product.getPrice(), productResponse.getPrice());
    Assertions.assertEquals(product.getCreator(), productResponse.getCreator());
  }

  @Test //Mockito 單元測試
  public void testGetProduct() {
    String productId = "123";
    Product testProduct = new Product();
    testProduct.setId(productId);
    testProduct.setName("Snack");
    testProduct.setPrice(50);
    testProduct.setCreator("abcd");

    //準備 ProductRepository 的模擬物件 返回剛建立testProduct
    ProductRepository productRepository = mock(ProductRepository.class);
    when(productRepository.findById(productId))
        .thenReturn(Optional.of(testProduct));
    System.out.println(testProduct);
    ProductService productService = new ProductService(productRepository, null);

    Product resultProduct = productService.getProduct(productId);
    System.out.println(resultProduct);
    Assertions.assertEquals(testProduct.getId(), resultProduct.getId());
    Assertions.assertEquals(testProduct.getName(), resultProduct.getName());
    Assertions.assertEquals(testProduct.getPrice(), resultProduct.getPrice());
    Assertions.assertEquals(testProduct.getCreator(), resultProduct.getCreator());
  }

  @Test
  public void testCreateProduct() {
    ProductRepository productRepository = mock(ProductRepository.class);
    UserIdentity userIdentity = mock(UserIdentity.class);

    String creatorId = "abc";
    when(userIdentity.getId()).thenReturn(creatorId);

    ProductService productService = new ProductService(productRepository, userIdentity);

    ProductRequest productReq = new ProductRequest();
    productReq.setName("Snack");
    productReq.setPrice(50);

    ProductResponse productRes = null;
    productRes = productService.createProduct(productReq);
//    productRes = productService.createProduct(productReq);
//    org.mockito.Mockito.verify(userIdentity,times(2)).getId();// verify 方法驗證模擬物件的getId是否有執行
//    org.mockito.Mockito.verify(productRepository,times(2)).insert(org.mockito.Mockito.any(Product.class));
    //傳入任何 Product 物件都可以,期望次數times

    InOrder inOrder = inOrder(productRepository, userIdentity);
    inOrder.verify(userIdentity, times(1)).getId();
    inOrder.verify(productRepository, times(1)).insert(org.mockito.Mockito.any(Product.class));

    Assertions.assertEquals(productReq.getName(), productRes.getName());
    Assertions.assertEquals(productReq.getPrice().intValue(), productRes.getPrice());
    Assertions.assertEquals(creatorId, productRes.getCreator());
  }
}
