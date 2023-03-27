package com.companyName.projectName.service;

import com.companyName.projectName.dao.MockProductDAO;
import com.companyName.projectName.entity.Product;
import com.companyName.projectName.exception.NotFoundException;
import com.companyName.projectName.exception.UnprocessableEntityException;
import com.companyName.projectName.modelAttribute.ProductQueryParameter;
import com.companyName.projectName.repository.ProductRepository;
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

    public Product createProduct(Product request) {
//        boolean isIdDuplicated = repository.findById(request.getId()).isPresent();
//        if (isIdDuplicated) {
//            throw new UnprocessableEntityException("The id of the product is duplicated.");
//        }

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return repository.insert(product);
    }

    public Product getProduct(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public Product replaceProduct(String id, Product request) {
        Product product = new Product();
        product.setId(getProduct(id).getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return repository.save(product);
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
