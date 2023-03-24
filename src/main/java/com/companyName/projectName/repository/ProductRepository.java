package com.companyName.projectName.repository;

import com.companyName.projectName.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByNameLike(String productName);
    // 找出 name 欄位值有包含參數的所有文件，且不分大小寫

    // 找出 id 欄位值有包含在參數之中的所有文件
    List<Product> findByIdIn(List<String> ids);

    List<Product> findByPriceBetweenAndNameLikeIgnoreCase(int priceFrom, int priceTo, String keyword, Sort sort);

    // 是否有文件的 email 欄位值等於參數
//    boolean existsByEmail(String email);

    // 找出 username 與 password 欄位值皆符合參數的一筆文件
//    Optional<User> findByUsernameAndPassword(String email, String pwd);

    // 查詢 price 欄位在特定範圍的文件（參數亦可使用 Date 資料）
// gte：大於等於；lte：小於等於；Between：大於及小於，兩者略有差異。
    @Query("{'price': {'$gte': ?0, '$lte': ?1}}")
    List<Product> findByPriceBetween(int from, int to);

    // 查詢 name 字串欄位有包含參數的文件，不分大小寫
    @Query("{'name': {'$regex': ?0, '$options': 'i'}}")
    List<Product> findByNameLikeIgnoreCase(String name);

    // 查詢同時符合上述兩個條件的文件
    @Query("{'$and': [{'price': {'$gte': ?0, '$lte': ?1}}, {'name': {'$regex': ?2, '$options': 'i'}}]}")
    List<Product> findByPriceBetweenAndNameLikeIgnoreCase(int priceFrom, int priceTo, String name);
}
