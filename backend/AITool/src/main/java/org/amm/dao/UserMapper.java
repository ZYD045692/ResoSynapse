package org.amm.dao;

import org.amm.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    // 新增用户
    public int insertUser(User user);

    // 修改密码
    public int updatePasswordHash(User user);

    //修改邮箱
    public int updateEmail(User user);

    //注销
    public int deleteUser(User user);

    // id查询
    public User findByUserId(User user);

    // 邮箱查询
    public User findByEmail(@Param("email") String email);

    // name查询
    public User findByUserName(String userName);

    //用户列表
    public List<User> userList();


}
