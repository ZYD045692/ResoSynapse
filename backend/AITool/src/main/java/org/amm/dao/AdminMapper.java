package org.amm.dao;

import org.amm.model.entity.Admin;


public interface AdminMapper {
    // 按adminName查询
    public Admin findByAdminName(String adminName);

    // 增
    public int insertAdmin(Admin admin);

    // 修改密码
    public int updateAdminPasswordHash(Admin admin);

    //注销
    public int deleteAdmin(Admin admin);

}
