package com.lxc.service.impl;

import com.lxc.dao.ProductDao;
import com.lxc.dao.ShoppingCartDao;
import com.lxc.entity.Product;
import com.lxc.entity.ShoppingCart;
import com.lxc.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/1/19
 */
@Service("shoppingCartService")
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public void updateShoppingCart(int user_id, int product_id, int num) {
        shoppingCartDao.updateShoppingCart(user_id,product_id,num);
    }

    @Override
    public void addShoppingCart(int user_id, int product_id) {
        shoppingCartDao.addShoppingCart(user_id,product_id);
    }

    @Override
    public ShoppingCart findShoppingCart(int user_id, int product_id) {
        return shoppingCartDao.findShoppingCart(user_id,product_id);
    }

    @Override
    public List<ShoppingCart> getShoppingCart(int user_id) {
        return shoppingCartDao.getShoppingCart(user_id);
    }

    /*获取购物车相关信息*/
    @Override
    public Map[] getShoppingCartData(List<ShoppingCart> shoppingCarts) {
        Map[] ShoppingCartData = new Map[shoppingCarts.size()];
        for(int i=0;i<ShoppingCartData.length;i++){
            ShoppingCart shoppingCart = shoppingCarts.get(i);
            Product product = productDao.getProductByID(shoppingCart.getProduct_id());
            HashMap<String, Object> tempData = new HashMap<>();
            tempData.put("id",shoppingCart.getId());
            tempData.put("productID",shoppingCart.getProduct_id());
            tempData.put("productName",product.getProduct_name());
            tempData.put("productImg",product.getProduct_picture());
            tempData.put("price",product.getProduct_selling_price());
            tempData.put("num",shoppingCart.getNum());
            tempData.put("maxNum",Math.floor(product.getProduct_num() / 2.0));
            tempData.put("check",true);
            ShoppingCartData[i] = tempData;
        }
        return ShoppingCartData;
    }

    @Override
    public boolean deleteShoppingCart(Integer user_id, Integer product_id) {
        if(null==user_id||null==product_id)
            return false;
        shoppingCartDao.deleteShoppingCart(user_id,product_id);
        return true;
    }
}
