package com.lxc.service;

import com.lxc.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author liuxianchun
 * @date 2021/1/17
 */
@Component
public interface UserService {

    User login(String userName,String formPass);
    int findUserByName(String userName);
    User registerUser(String userName,String formPass);
    void updateUser(User user);

}
