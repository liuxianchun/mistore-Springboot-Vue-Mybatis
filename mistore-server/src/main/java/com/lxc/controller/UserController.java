package com.lxc.controller;

import com.lxc.entity.User;
import com.lxc.service.UserService;
import com.lxc.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register")
    public Map userRegister(@RequestBody Map<String,String> userMap,HttpServletRequest req,HttpServletResponse res){
        String userName = userMap.get("userName");
        String password = userMap.get("password");
        HashMap map = new HashMap<String,String>();
        if(userService.findUserByName(userName)>0){
            map.put("code","004");
            map.put("msg","用户名已存在，不能注册");
        }else{
            User user = userService.registerUser(userName, password);
            map.put("code","001");
            map.put("msg","注册成功");
            String ticket = UUID.randomUUID().toString();
            HttpSession session = req.getSession();
            session.setAttribute(ticket,user);
            CookieUtil.setCookie(req,res,"userTicket",ticket,-1,false);
            log.info("用户"+user.getUserName()+"注册成功:"+ticket);
        }
        return map;
    }

    @RequestMapping(value = "/findUserName")
    public Map findByName(@RequestBody Map<String,String> userMap){
        String userName = userMap.get("userName");
        HashMap map = new HashMap<String,String>();
        if(userService.findUserByName(userName)>0){
            map.put("code","004");
            map.put("msg","用户名已存在，不能注册");
        }else{
            map.put("code","001");
            map.put("msg","可以注册");
        }
        return map;
    }

    @PostMapping(value = "/login")
    public Map userLogin(@RequestBody Map<String,String> userMap, HttpServletRequest req, HttpServletResponse res){
        String userName = userMap.get("userName");
        String password = userMap.get("password");
        HashMap map = new HashMap<String,String>();
        User user = userService.login(userName, password);
        if(user!=null){
            map.put("code","001");
            map.put("msg","登录成功");
            map.put("user",user);
            String ticket = UUID.randomUUID().toString();
            HttpSession session = req.getSession();
            session.setAttribute(ticket,user);
            CookieUtil.setCookie(req,res,"userTicket",ticket,-1,false);
            log.info("用户"+user.getUserName()+"登录:"+ticket);
        }else{
            map.put("code","004");
            map.put("msg","登录失败");
        }
        return map;
    }

    @RequestMapping(value = "/updateUser")
    public Map updateUser(User user){
        HashMap map = new HashMap<String,String>();
        userService.updateUser(user);
        map.put("code","001");
        map.put("msg","修改成功");
        return map;
    }

    @RequestMapping(value = "/logout")
    public void userLogout(@RequestBody Map<String,String> map, HttpServletRequest req){
        String userTicket = CookieUtil.getCookieValue(req, "userTicket", false);
        req.getSession().removeAttribute(userTicket);
        log.info("用户"+map.get("username")+"退出登录");
    }

}
