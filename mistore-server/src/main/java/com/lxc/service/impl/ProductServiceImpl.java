package com.lxc.service.impl;

import com.lxc.dao.ProductDao;
import com.lxc.entity.Category;
import com.lxc.entity.Product;
import com.lxc.entity.Product_picture;
import com.lxc.service.ProductService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/1/18
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public List<Category> getCategory() {
        return productDao.getCategory();
    }

    @Override
    public List<Product> getAllProduct() {
        Map<Object, Object> products = redisTemplate.opsForHash().entries("products");
        if(null==products||products.size()==0){    //redis中无数据，查询数据库
            List<Product> allProduct = productDao.getAllProduct();
            products.put("products",allProduct);
            redisTemplate.opsForHash().putAll("products",products);
        }
        return (List<Product>) products.get("products");
    }

    @Override
    public List getCategory_id() {
        Map<Object, Object> category_id = redisTemplate.opsForHash().entries("category_id");
        if(category_id.isEmpty()){
            List category_idList = productDao.getCategory_id();
            category_id.put("category_id",category_idList);
            redisTemplate.opsForHash().putAll("category_id",category_id);
        }
        return (List) category_id.get("category_id");
    }

    @Override
    public List<Product> getProductByCategory(int categoryID,int currentPage,int pageSize) {
        return productDao.getProductByCategory(categoryID,currentPage,pageSize);
    }

    @Override
    public List<Product> getProductByCategoryName(String categoryName) {
        return productDao.getProductByCategoryName(categoryName);
    }

    @Override
    public Product getProductByID(int product_id) {
        Map<Object, Object> productMap = redisTemplate.opsForHash().entries("product");
        if(productMap.isEmpty()){   //redis无map
            Product product = productDao.getProductByID(product_id);
            productMap.put(product_id+"",product);
            redisTemplate.opsForHash().putAll("product",productMap);
        }else if(productMap.get(product_id)==null){   //redis无该商品
            productMap.put(product_id+"",productDao.getProductByID(product_id));
            redisTemplate.opsForHash().putAll("product",productMap);
        }
        return (Product) productMap.get(product_id+"");
    }

    @Override
    public List<Product_picture> getDetailsPicture(int product_id) {
        return productDao.getDetailsPicture(product_id);
    }

    @Override
    public List<Product> getProductBySearch(String search) {
        return productDao.getProductBySearch(search);
    }
}
