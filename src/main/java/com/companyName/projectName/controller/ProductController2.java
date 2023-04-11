package com.companyName.projectName.controller;

import com.companyName.projectName.entity.Product;
import com.companyName.projectName.modelAttribute.ProductQueryParameter;
import com.companyName.projectName.request.ProductRequest;
import com.companyName.projectName.response.ProductResponse;
import com.companyName.projectName.service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController2 {
    @Autowired
    private ProductService productService;

//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {
//        Product product = productService.getProduct(id);
//        return ResponseEntity.ok(product);
//    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@ModelAttribute ProductQueryParameter param) {
        List<Product> products = productService.getProducts(param);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
        @Parameter(description = "ID of product.")
        @PathVariable("id")
        String id
    ) {
        ProductResponse product = productService.getProductResponse(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.createProduct(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> replaceProduct(
            @PathVariable("id") String id, @Valid @RequestBody ProductRequest request) {
        Product product = productService.replaceProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
