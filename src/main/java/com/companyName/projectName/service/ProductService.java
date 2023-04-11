package com.companyName.projectName.service;

import com.companyName.projectName.annotaion.SendEmail;
import com.companyName.projectName.converter.ProductConverter;
import com.companyName.projectName.dao.MockProductDAO;
import com.companyName.projectName.entity.Product;
import com.companyName.projectName.enums.ActionType;
import com.companyName.projectName.enums.EntityType;
import com.companyName.projectName.exception.NotFoundException;
import com.companyName.projectName.login.auth.UserIdentity;
import com.companyName.projectName.modelAttribute.ProductQueryParameter;
import com.companyName.projectName.repository.ProductRepository;
import com.companyName.projectName.request.ProductRequest;
import com.companyName.projectName.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

public class ProductService {

    private UserIdentity userIdentity;
    @Autowired
    private MockProductDAO productDAO;
    private final ProductRepository repository;

    public ProductService(ProductRepository repository, UserIdentity userIdentity) {
        this.repository = repository;
        this.userIdentity = userIdentity;
    }

    @SendEmail(entity = EntityType.PRODUCT, action = ActionType.CREATE)
    public ProductResponse createProduct(ProductRequest request) {
        Product product = ProductConverter.toProduct(request);
        product.setCreator(userIdentity.getId());
        repository.insert(product);

        return ProductConverter.toProductResponse(product);
    }

    public Product getProduct(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    @SendEmail(entity = EntityType.PRODUCT, action = ActionType.UPDATE, idParamIndex = 0)
    public Product replaceProduct(String id, ProductRequest request) {
        Product oldProduct = getProduct(id);
        Product newProduct = ProductConverter.toProduct(request);
        newProduct.setId(oldProduct.getId());

        return repository.save(newProduct);
    }

    @SendEmail(entity = EntityType.PRODUCT, action = ActionType.DELETE, idParamIndex = 0)
    public void deleteProduct(String id) {
        Product product = getProduct(id);
        repository.deleteById(product.getId());
    }

    public List<Product> getProducts(ProductQueryParameter param) {
        String keyword = Optional.ofNullable(param.getKeyword()).orElse("");
        int priceFrom = Optional.ofNullable(param.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(param.getPriceTo()).orElse(Integer.MAX_VALUE);

        Sort sort = genSortingStrategy(param.getOrderBy(), param.getSortRule());

        return repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, keyword, sort);
    }

    private Sort genSortingStrategy(String orderBy, String sortRule) {
        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
            sort = Sort.by(direction, orderBy);
        }

        return sort;
    }

    public ProductResponse getProductResponse(String id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
        return ProductConverter.toProductResponse(product);
    }
}
