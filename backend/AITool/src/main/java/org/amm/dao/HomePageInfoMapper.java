package org.amm.dao;

import org.amm.model.entity.HomePageInfo;

public interface HomePageInfoMapper {
    // 返回数据表全部内容（就tm一行）
    HomePageInfo findByPageName(String pageName);

    // 修改page信息
    int updateHomePageInfo(HomePageInfo homePageInfo);

}
