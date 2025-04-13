package org.amm.controller;

import org.amm.model.dto.BlockWidgetUserDTO;
import org.amm.model.entity.HomePageInfo;
import org.amm.model.entity.WidgetRating;
import org.amm.model.entity.EmailNotification;
import org.amm.model.entity.WidgetUsageLog;
import org.amm.service.IUserPageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/userPage")
public class UserPageController {
    @Autowired
    private IUserPageService userPageService;

    //返回homepage信息
    @RequestMapping(value = "/getUserHomePageInfo", method = RequestMethod.GET)
    @ResponseBody
    public HomePageInfo getUserHomePageInfo() {
        return userPageService.userHomePageInfo("HomePage");
    }

    // 板块获取
    @RequestMapping(value="/getUserBlocks", method = RequestMethod.GET)
    @ResponseBody
    public List<BlockWidgetUserDTO> getUserBlocks() {
        return userPageService.getUserBlocks();
    }

    // 用户评价
    @RequestMapping(value="/userFeedback", method=RequestMethod.POST)
    @ResponseBody
    public Map userFeedback(@RequestBody WidgetRating widgetRating) {
        Map map = new HashMap<String, String>();
        map.put("stateCode", userPageService.userFeedback(widgetRating));
        return map;
    }

    // 申请收录
    @RequestMapping(value="/userApplyUrl", method = RequestMethod.POST)
    @ResponseBody
    public Map userApplyUrl(@RequestBody EmailNotification emailNotification) {
        Map map = new HashMap<String, String>();
        map.put("stateCode", userPageService.userApplyUrl(emailNotification));
        return map;
    }

    // 用户使用日志
    @RequestMapping(value="/userUseModuleLog", method = RequestMethod.POST)
    @ResponseBody
    public void userUseModuleLog(@RequestBody WidgetUsageLog widgetUsageLog) {
        userPageService.userUseModuleLog(widgetUsageLog);
    }





}
