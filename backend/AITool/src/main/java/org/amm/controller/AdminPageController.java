package org.amm.controller;

import org.amm.config.ContextPathConfig;
import org.amm.config.FileConfig;
import org.amm.dao.HomePageInfoMapper;
import org.amm.model.dto.BlockWidgetAdminDTO;
import org.amm.model.entity.BlockWidget;
import org.amm.model.entity.HomePageInfo;
import org.amm.model.entity.ModuleWidget;
import org.amm.service.IAdminPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adminPage")
public class AdminPageController {
    @Autowired
    private IAdminPageService adminPageService;
    @Autowired
    private HomePageInfoMapper homePageInfoMapper;

    // 板块获取
    @RequestMapping(value = "/getAdminBlocks", method = RequestMethod.GET)
    @ResponseBody
    public List<BlockWidgetAdminDTO> getAdminBlocks(@RequestParam int day) {
        return adminPageService.getAdminBlocks(day);
    }

    // 板块顺序修改
    @RequestMapping(value = "/updateBlockOrder", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateBlockOrder(@RequestBody List<Map<String, Object>> updateBlockOrderList) {
        Map<String, Object> map = new HashMap<String, Object>();
        int stateCode = adminPageService.updateBlockOrder(updateBlockOrderList);
        map.put("stateCode", stateCode);
        return map;
    }

    // 板块添加
    @RequestMapping(value = "/addBlockWidget", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addBlockWidget(@RequestBody BlockWidget blockWidget) {
        Map<String, Object> map = new HashMap<String , Object>();
        int stateCode = adminPageService.addBlockWidget(blockWidget);
        map.put("stateCode", stateCode);
        return map;
    }

    // 板块信息修改
    @RequestMapping(value = "/updateBlockInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateBlockInfo(@RequestBody BlockWidget newBlockInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        int stateCode = adminPageService.updateBlockInfo(newBlockInfo);
        map.put("stateCode", stateCode);
        return map;
    }

    // 模块顺序修改
    @RequestMapping(value = "/updateModuleOrder", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateModuleOrder(@RequestBody List<Map<String, Object>> updateModuleOrderList) {
        Map<String, Object> map = new HashMap<String, Object>();
        int stateCode = adminPageService.updateModuleOrder(updateModuleOrderList);
        map.put("stateCode", stateCode);
        return map;
    }

    // 模块添加
    @RequestMapping(value = "/addModuleWidget", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addModuleWidget(@RequestBody ModuleWidget moduleWidget) {
        Map<String, Object> map = new HashMap<String, Object>();
        int stateCode = adminPageService.addModuleWidget(moduleWidget);
        map.put("stateCode", stateCode);
        return map;
    }

    // 模块信息修改
    @RequestMapping(value = "/updateModuleInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateModuleInfo(@RequestBody ModuleWidget newModuleInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        int stateCode = adminPageService.updateModuleInfo(newModuleInfo);
        map.put("stateCode", stateCode);
        return map;
    }

    // homepage信息修改
    @RequestMapping(value = "/updateHomePageInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateHomePageInfo(@RequestBody HomePageInfo newHomePageInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        int stateCode = homePageInfoMapper.updateHomePageInfo(newHomePageInfo);
        map.put("stateCode", stateCode);
        return map;
    }

}
