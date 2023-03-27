package com.companyName.projectName.converter;

import com.companyName.projectName.entity.Product;
import com.companyName.projectName.request.ProductRequest;

/*proxy*/
public class ProductConverter {

    private ProductConverter() {
    }

    public static Product toProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return product;
    }

}
