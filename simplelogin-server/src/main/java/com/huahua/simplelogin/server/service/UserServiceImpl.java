package com.huahua.simplelogin.server.service;

import com.huahua.simplelogin.client.entry.User;
import com.huahua.simplelogin.server.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;

    @Value("user")
    private String table;
    @Value("id")
    private String userid;
    @Value("username")
    private String username;
    @Value("password")
    private String password;

    @Override
    public User find(User user) {
        return userDao.find(user,table,userid,username,password);
    }
}
