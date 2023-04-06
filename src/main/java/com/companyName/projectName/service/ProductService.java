package com.companyName.projectName.service;

import com.companyName.projectName.converter.ProductConverter;
import com.companyName.projectName.dao.MockProductDAO;
import com.companyName.projectName.entity.Product;
import com.companyName.projectName.exception.NotFoundException;
import com.companyName.projectName.modelAttribute.ProductQueryParameter;
import com.companyName.projectName.repository.ProductRepository;
import com.companyName.projectName.request.ProductRequest;
import com.companyName.projectName.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class ProductService {


    @Autowired
    private MockProductDAO productDAO;
    private final ProductRepository repository;
    private final MailService mailService;

    public ProductService(ProductRepository repository, MailService mailService) {
        this.repository = repository;
        this.mailService = mailService;
    }

    public ProductResponse createProduct(ProductRequest request) {
//        boolean isIdDuplicated = repository.findById(request.getId()).isPresent();
//        if (isIdDuplicated) {
//            throw new UnprocessableEntityException("The id of the product is duplicated.");
//        }

//        Product product = ProductConverter.toProduct(request);
//        return repository.insert(product);

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product = repository.insert(product);

        return ProductConverter.toProductResponse(product);
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

    public ProductResponse getProductResponse(String id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
        return ProductConverter.toProductResponse(product);
    }
}
