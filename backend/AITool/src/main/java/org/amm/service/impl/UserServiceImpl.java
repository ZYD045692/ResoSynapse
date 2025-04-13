package org.amm.service.impl;

import org.amm.dao.UserMapper;
import org.amm.model.entity.User;
import org.amm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    // 按照Name查询用户
    @Override
    @Transactional
    public User findByUserName(String userName){
        return userMapper.findByUserName(userName);
    }


    //登录
    @Override
    @Transactional
    public Map<String, Object> login(User user){
        Map<String, Object> map = new HashMap<>();
        User userTemp = userMapper.findByUserName(user.getUserName());
        if(userTemp == null){
            map.put("stateCode", 0);    // 用户不存在
            map.put("userId", -1);
            return map;
        } else if (!user.getPasswordHash().equals(userTemp.getPasswordHash())){
            map.put("stateCode", 1);    // 密码错误
            map.put("userId", -1);
            return map;
        }
        map.put("stateCode", 2);    //登录成功
        map.put("userId", userTemp.getUserId());
        map.put("profilePhotoUrl", userTemp.getProfilePhotoUrl());
        return map;
    }

    //注册
    @Override
    @Transactional
    public int register(User user){
        if(userMapper.findByUserName(user.getUserName()) != null){
            return 0;    //用户名重复
        } else if(userMapper.findByEmail(user.getEmail()) != null){
            return 1;    //邮箱重复
        } else {
            userMapper.insertUser(user);
            return 2;    //注册成功
        }

    }

    //修改密码
    @Override
    @Transactional
    public void updatePasswordHash(User user){
        int result = userMapper.updatePasswordHash(user);
    }

    //修改邮箱
    @Override
    @Transactional
    public void updateEmail(User user){
        int result = userMapper.updateEmail(user);

    }


    //注销
    @Override
    @Transactional
    public void deleteUser(User user) {
        int result = userMapper.deleteUser(user);
    }

}
