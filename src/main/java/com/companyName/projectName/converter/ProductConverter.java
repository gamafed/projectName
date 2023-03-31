package com.companyName.projectName.converter;

import com.companyName.projectName.entity.Product;
import com.companyName.projectName.request.ProductRequest;
import com.companyName.projectName.response.ProductResponse;
import lombok.NoArgsConstructor;

/*proxy*/
@NoArgsConstructor
public class ProductConverter {

    public static Product toProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return product;
    }

    public static ProductResponse toProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        return response;
    }

}
