package com.lxc.service;

import com.lxc.entity.Category;
import com.lxc.entity.Product;
import com.lxc.entity.Product_picture;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/1/18
 */
@Component
public interface ProductService {

    List<Category> getCategory();

    List<Product> getAllProduct();

    List getCategory_id();

    List<Product> getProductByCategory(int categoryID,int currentPage,int pageSize);

    List<Product> getProductByCategoryName(String categoryName);

    Product getProductByID(int product_id);

    List<Product_picture> getDetailsPicture(int product_id);

    List<Product> getProductBySearch(String search);
}
