package org.amm.service.impl;

import org.amm.config.ContextPathConfig;
import org.amm.dao.*;
import org.amm.model.entity.*;
import org.amm.model.dto.*;
import org.amm.service.IUserPageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.amm.config.IPConfig;
import java.util.List;

@Service
public class UserPageServiceImpl implements IUserPageService {
    @Autowired
    private HomePageInfoMapper homePageInfoMapper;
    @Autowired
    private BlockWidgetMapper blockWidgetMapper;
    @Autowired
    private WidgetRatingMapper widgetRatingMapper;
    @Autowired
    private EmailNotificationMapper emailNotificationMapper;
    @Autowired
    private WidgetUsageLogMapper widgetUsageLogMapper;
    @Autowired
    private IPConfig Ip;
    @Autowired
    private ContextPathConfig contextPathConfig;


    public String getwholePath(String url){
        String ip = Ip.getIp();
        String contextPath = contextPathConfig.getContextPath();
        if(url.startsWith(contextPath)){
            return url;
        }else{
            return ip+contextPath+'/'+url;
        }
    }

    @Override
    public HomePageInfo userHomePageInfo(String pageName){
        HomePageInfo homePageInfo = homePageInfoMapper.findByPageName(pageName);
        homePageInfo.setBackgroundUrl(getwholePath(homePageInfo.getBackgroundUrl()));
        return homePageInfo;
    }

    @Override
    public List<BlockWidgetUserDTO> getUserBlocks(){
        List<BlockWidgetUserDTO> blockWidgetUserDTOs = blockWidgetMapper.getUserWidgetMap();
        for (int i = 0; i < blockWidgetUserDTOs.size(); i++){
            // 动态修改大组件ip
            blockWidgetUserDTOs.get(i).setIconUrl(getwholePath(blockWidgetUserDTOs.get(i).getIconUrl()));
            for (int j = 0; j< blockWidgetUserDTOs.get(i).getModuleWidgetUserDTOs().size(); j++) {
                // 动态修改小组件ip
                blockWidgetUserDTOs.get(i).getModuleWidgetUserDTOs().get(j).setIconUrl(getwholePath(blockWidgetUserDTOs.get(i).getModuleWidgetUserDTOs().get(j).getIconUrl()));
            }
        }
        return blockWidgetUserDTOs;
    }

    @Override
    @Transactional
    public int userFeedback(WidgetRating widgetRating){
        int result = widgetRatingMapper.insertWidgetRating(widgetRating);
        if (result == 1){
            return 1;  //评价成功
        } else {
            return 0;   //评价失败
        }
    }

    @Override
    @Transactional
    public int userApplyUrl(EmailNotification emailNotification){
        int result = emailNotificationMapper.insertEmail(emailNotification);
        if (result == 1){
            return 1;    //提交成功
        } else {
            return 0;    //提交失败
        }
    }

    @Override
    @Transactional
    public int userUseModuleLog(WidgetUsageLog widgetUsageLog){
        int result = widgetUsageLogMapper.insertWidgetUsageLog(widgetUsageLog);
        if (result == 1){
            return 1;
        } else {
            return 0;
        }
    }


}
