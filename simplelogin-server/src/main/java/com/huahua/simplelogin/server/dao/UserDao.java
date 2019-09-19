package com.huahua.simplelogin.server.dao;

import com.huahua.simplelogin.client.entry.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    public User find(@Param("user") User user, @Param("table") String table,@Param("userid") String userid,
                     @Param("username") String username, @Param("password") String password);


}
