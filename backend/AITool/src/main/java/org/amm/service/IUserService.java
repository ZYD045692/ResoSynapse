package org.amm.service;

import org.amm.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface IUserService {
    //按照Name查询用户
    User findByUserName(String userName);

    //注册
    @Transactional
    int register(User user);

    //登录
    @Transactional
    Map<String, Object> login(User user);

    //修改密码
    @Transactional
    void updatePasswordHash(User user);

    //修改邮箱
    @Transactional
    void updateEmail(User user);

    //注销
    @Transactional
    void deleteUser(User user);
}
