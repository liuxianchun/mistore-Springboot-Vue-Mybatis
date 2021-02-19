package com.lxc.service.impl;

import com.lxc.dao.UserDao;
import com.lxc.entity.User;
import com.lxc.service.UserService;
import com.lxc.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Map;
import java.util.UUID;

/**
 * @author liuxianchun
 * @date 2021/1/17
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public User login(String userName, String formPass) {
        String salt = userDao.getSalt(userName);
        if(StringUtils.isEmpty(salt))
            return null;
        String DBPass = MD5Util.formPassToDBPass(formPass,salt);
        User user = userDao.login(userName, DBPass);
        if(user!=null){
            Map<Object, Object> login_count = redisTemplate.opsForHash().entries("login_count");
            if(login_count.isEmpty()||login_count.get(userName)==null){
                login_count.put(userName,1);
            }else{
                login_count.put(userName,(int) login_count.get(userName)+1);
            }
            redisTemplate.opsForHash().putAll("login_count",login_count);
        }
        return user;
    }

    @Override
    public int findUserByName(String userName) {
        return userDao.findUserByName(userName);
    }

    @Override
    public User registerUser(String userName, String formPass) {
        String salt = UUID.randomUUID().toString().replace("-","").substring(0,15);
        String DBPass = MD5Util.formPassToDBPass(formPass, salt);
        userDao.registerUser(userName,DBPass,salt);
        return userDao.findByUserName(userName);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }
}
