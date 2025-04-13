package org.amm.service.impl;

import org.amm.dao.AdminMapper;
import org.amm.model.entity.Admin;
import org.amm.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;

    //登录
    @Override
    @Transactional
    public int login(Admin admin) {
        Admin adminTemp = adminMapper.findByAdminName(admin.getAdminName());
        if (adminTemp == null) {
            return 0;    //管理员不存在
        } else if(!admin.getAdminPasswordHash().equals(adminTemp.getAdminPasswordHash())) {
            return 1;    //密码错误
        }
        return 2;    //密码正确
    }

    // 注册
    @Override
    @Transactional
    public int register(Admin admin) {
        Admin adminTemp = adminMapper.findByAdminName(admin.getAdminName());
        if (adminTemp == null) {
            return 0;    // 管理员名重复
        } else {
            int result = adminMapper.insertAdmin(admin);
            if (result == 1) {
                return 1;  //注册成功
            } else {
                return 0;  //操作失败
            }
        }
    }

    //修改密码
    @Override
    @Transactional
    public void updateAdminPasswordHash(Admin admin) {
        int result = adminMapper.updateAdminPasswordHash(admin);
    }

    //注销
    @Override
    @Transactional
    public void deleteAdmin(Admin admin) {
        int result = adminMapper.deleteAdmin(admin);
    }

    @Override
    @Transactional
    public Admin findByAdminName(String adminName) {
        return adminMapper.findByAdminName(adminName);
    }
}
