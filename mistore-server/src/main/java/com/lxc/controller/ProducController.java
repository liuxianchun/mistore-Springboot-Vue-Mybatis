package com.lxc.controller;

import com.lxc.entity.Category;
import com.lxc.entity.Product;
import com.lxc.entity.Product_picture;
import com.lxc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/1/18
 */
@RestController
@RequestMapping(value = "/product")
public class ProducController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/getCategory")
    public Map getCategory(){
        List<Category> category = productService.getCategory();
        HashMap<String, Object> map = new HashMap<>();
        map.put("code","001");
        map.put("category",category);
        return map;
    }

    @RequestMapping(value = "/getAllProduct")
    public Map getAllProduct(@RequestBody Map<String,Object> paramMap){
        List<Product> product = productService.getAllProduct();
        HashMap<String, Object> map = new HashMap<>();
        map.put("Product",product);
        map.put("total",product.size());
        return map;
    }

    @RequestMapping(value = "/getProductByCategory")
    public Map getProductByCategory(@RequestBody Map<String,Object> paramMap){
        ArrayList categoryIDList = (ArrayList) paramMap.get("categoryID");
        int categoryID = (int) categoryIDList.get(0);
        int currentPage = (int)paramMap.get("currentPage");
        int pageSize = (int)paramMap.get("pageSize");
        List<Product> product = productService.getProductByCategory(categoryID,currentPage,pageSize);
        HashMap<String, Object> map = new HashMap<>();
        map.put("Product",product);
        return map;
    }

    @RequestMapping(value = "/getPromoProduct")
    public Map getPromoProduct(@RequestBody Map<String,String> paramMap){
        String categoryName = paramMap.get("categoryName");
        List<Product> product = productService.getProductByCategoryName(categoryName);
        HashMap<String, Object> map = new HashMap<>();
        map.put("Product",product);
        return map;
    }

    @RequestMapping(value = "/getHotProduct")
    public Map getHotProduct(@RequestBody Map<String,List<String>> paramMap){
        List<String> categoryName = paramMap.get("categoryName");
        List<Product> product = new ArrayList<>();
        for(String name:categoryName){
            List<Product> tempProduct = productService.getProductByCategoryName(name);
            product.addAll(tempProduct);   //添加多个类型产品
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("Product",product);
        return map;
    }

    @RequestMapping(value = "/getDetails")
    public Map getDetails(@RequestBody Map<String,Integer> paramMap){
        int productID = paramMap.get("productID");
        Product product = productService.getProductByID(productID);
        HashMap<String, Object> map = new HashMap<>();
        map.put("Product",new ArrayList<Product>(){{this.add(product);}});
        return map;
    }

    @RequestMapping(value = "/getDetailsPicture")
    public Map getDetailsPicture(@RequestBody Map<String,Integer> paramMap){
        int productID = paramMap.get("productID");
        List<Product_picture> ProductPicture = productService.getDetailsPicture(productID);
        HashMap<String, Object> map = new HashMap<>();
        map.put("ProductPicture",ProductPicture);
        return map;
    }

    @RequestMapping(value = "/getProductBySearch")
    public Map getProductBySearch(@RequestBody Map<String,Object> paramMap){
        String search = (String) paramMap.get("search");
        int currentPage = (int) paramMap.get("currentPage");
        int pageSize = (int) paramMap.get("pageSize");
        int offset = (currentPage-1)*pageSize;
        List<Category> categorys = productService.getCategory();
        HashMap<String, Object> map = new HashMap<>();
        for(Category category:categorys){
            if(category.getCategory_name().equals(search)){
                List<Product> product = productService.getProductByCategory(category.getCategory_id(), offset, pageSize);
                map.put("code","001");
                map.put("Product",product);
                map.put("total",product.size());
                return map;
            }
        }
        List<Product> product = productService.getProductBySearch(search);
        map.put("code","001");
        map.put("Product",product);
        map.put("total",product.size());
        return map;
    }
}
