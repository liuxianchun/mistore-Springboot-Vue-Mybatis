package com.lxc.controller;

import com.lxc.entity.Order;
import com.lxc.entity.Product;
import com.lxc.service.OrderService;
import com.lxc.service.ProductService;
import com.lxc.service.ShoppingCartService;
import com.lxc.utils.CookieUtil;
import com.lxc.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author liuxianchun
 * @date 2021/1/21
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/getOrder")
    public Map getOrder(@RequestBody Map<String,Object> paramMap, HttpServletRequest req){
        String ticket = CookieUtil.getCookieValue(req, "userTicket", false);
        if(StringUtils.isEmpty(ticket)||req.getSession().getAttribute(ticket)==null){
            return RespBeanEnum.WITHOUT_LOGIN.getMap();
        }
        Integer user_id = (Integer) paramMap.get("user_id");
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<Long> Order_Ids = orderService.getOrderIdByUser(user_id);  //获取订单id
        if(Order_Ids==null){
            map.put("code","002");
            map.put("msg","该用户没有订单信息");
        }else{
            List<List<Order>> ordersList = new ArrayList<>();   //需要返回前端的list
            for(Long order_id:Order_Ids){
                List<Order> tempList = new ArrayList<>();
                List<Order> orders = orderService.getOrders(order_id);   //同一笔订单的商品
                for(Order order:orders){
                    Product product = productService.getProductByID(order.getProduct_id());
                    order.setProduct_name(product.getProduct_name());
                    order.setProduct_picture(product.getProduct_picture());
                    tempList.add(order);
                }
                ordersList.add(tempList);
            }
            map.put("orders",ordersList);
            map.put("code","001");
        }
        return map;
    }

    @RequestMapping(value = "/addOrder")
    public Map addOrder(@RequestBody Map<String,Object> paramMap, HttpServletRequest req){
        String ticket = CookieUtil.getCookieValue(req, "userTicket", false);
        if(StringUtils.isEmpty(ticket)||req.getSession().getAttribute(ticket)==null){
            return RespBeanEnum.WITHOUT_LOGIN.getMap();
        }
        Integer user_id = (Integer) paramMap.get("user_id");
        List<Map> products = (List<Map>) paramMap.get("products");
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(user_id==null||products==null){
            map.put("code","004");
            map.put("msg","未找到订单");
        }else{
            Long orderId = orderService.getOrderId();
            for (Map<String,Object> product : products) {
                boolean flag = (Boolean)product.get("check");
                if (flag) {  //已勾选的商品
                    int product_id = (int)(product.get("productID"));
                    BigDecimal product_price = new BigDecimal((Integer)product.get("price"));
                    int product_num = (int)(product.get("num"));
                    orderService.addOrder(orderId,user_id,product_id,product_num,product_price);  //添加订单
                    shoppingCartService.deleteShoppingCart(user_id,product_id);   //删除购物车
                }
            }
            map.put("code","001");
            map.put("msg","购买成功");
        }
        return map;
    }

    @RequestMapping(value = "/orderPay")
    public Map orderPay(@RequestBody Map<String,Object> paramMap, HttpServletRequest req){
        String ticket = CookieUtil.getCookieValue(req, "userTicket", false);
        if(StringUtils.isEmpty(ticket)||req.getSession().getAttribute(ticket)==null){
            return RespBeanEnum.WITHOUT_LOGIN.getMap();
        }
        Integer user_id = (Integer) paramMap.get("user_id");
        Long order_id = (Long) paramMap.get("order_id");
        HashMap<String, String> map = new HashMap<>();
        int excute = orderService.orderPay(user_id, order_id);
        if(excute>0){
            map.put("code","001");
            map.put("msg","付款成功");
        }else{
            map.put("code","004");
            map.put("msg","付款失败");
        }
        return map;
    }

}
