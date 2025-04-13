package org.amm.service;

import org.amm.model.entity.Admin;
import org.springframework.transaction.annotation.Transactional;

public interface IAdminService {
    //登录
    @Transactional
    int login(Admin admin);

    //注册
    @Transactional
    int register(Admin admin);

    //修改密码
    @Transactional
    void updateAdminPasswordHash(Admin admin);

    //注销
    @Transactional
    void deleteAdmin(Admin admin);

    //按adminName查询
    @Transactional
    Admin findByAdminName(String adminName);

}
