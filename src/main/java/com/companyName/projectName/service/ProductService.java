package com.companyName.projectName.service;

import com.companyName.projectName.converter.ProductConverter;
import com.companyName.projectName.dao.MockProductDAO;
import com.companyName.projectName.entity.Product;
import com.companyName.projectName.exception.NotFoundException;
import com.companyName.projectName.exception.UnprocessableEntityException;
import com.companyName.projectName.modelAttribute.ProductQueryParameter;
import com.companyName.projectName.repository.ProductRepository;
import com.companyName.projectName.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private MockProductDAO productDAO;
    @Autowired
    private ProductRepository repository;

    public Product createProduct(ProductRequest request) {
//        boolean isIdDuplicated = repository.findById(request.getId()).isPresent();
//        if (isIdDuplicated) {
//            throw new UnprocessableEntityException("The id of the product is duplicated.");
//        }

        Product product = ProductConverter.toProduct(request);

        return repository.insert(product);
    }

    public Product getProduct(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public Product replaceProduct(String id, ProductRequest request) {
        Product oldProduct = getProduct(id);
        Product newProduct = ProductConverter.toProduct(request);
        newProduct.setId(oldProduct.getId());

        return repository.save(newProduct);
    }

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

}
