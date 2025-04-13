package org.amm.service;

import org.amm.model.entity.*;
import org.amm.model.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUserPageService {
    // 返回页面数据
    HomePageInfo userHomePageInfo(String pageName);

    //板块获取
    List<BlockWidgetUserDTO> getUserBlocks();

    // 用户评价指标
    @Transactional
    int userFeedback(WidgetRating widgetRating);

    // 申请收录
    @Transactional
    int userApplyUrl(EmailNotification emailNotification);

    // 增加使用日志
    @Transactional
    int userUseModuleLog(WidgetUsageLog widgetUsageLog);

}
