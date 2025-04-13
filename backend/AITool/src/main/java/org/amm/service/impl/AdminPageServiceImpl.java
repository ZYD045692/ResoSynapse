package org.amm.service.impl;

import org.amm.config.ContextPathConfig;
import org.amm.dao.*;
import org.amm.model.dto.BlockWidgetAdminDTO;
import org.amm.model.entity.BlockWidget;
import org.amm.model.entity.HomePageInfo;
import org.amm.model.entity.ModuleWidget;
import org.amm.service.IAdminPageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.amm.config.IPConfig;
@Service
public class AdminPageServiceImpl implements IAdminPageService {
    @Autowired
    private BlockWidgetMapper blockWidgetMapper;
    @Autowired
    private WidgetRatingMapper widgetRatingMapper;
    @Autowired
    private WidgetUsageLogMapper widgetUsageLogMapper;
    @Autowired
    private ModuleWidgetMapper moduleWidgetMapper;
    @Autowired
    private HomePageInfoMapper homePageInfoMapper;

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
    public List<BlockWidgetAdminDTO> getAdminBlocks(int day){
        List<BlockWidgetAdminDTO> blockWidgetAdminDTOs = blockWidgetMapper.getAdminWidgetMap();
        for (int i = 0; i < blockWidgetAdminDTOs.size(); i++){
            // 动态修改大组件ip
            blockWidgetAdminDTOs.get(i).setIconUrl(getwholePath(blockWidgetAdminDTOs.get(i).getIconUrl()));
            blockWidgetAdminDTOs.get(i).setImgUrl(getwholePath(blockWidgetAdminDTOs.get(i).getImgUrl()));

            for (int j = 0; j< blockWidgetAdminDTOs.get(i).getModuleWidgetAdminDTOs().size(); j++) {
                // 动态修改小组件ip
                blockWidgetAdminDTOs.get(i).getModuleWidgetAdminDTOs().get(j).setIconUrl(getwholePath(blockWidgetAdminDTOs.get(i).getModuleWidgetAdminDTOs().get(j).getIconUrl()));
                // 计算点击次数
                int clickCount = widgetUsageLogMapper.calClickCount(blockWidgetAdminDTOs.get(i).getModuleWidgetAdminDTOs().get(j).getId(), day);
                // 计算平均评价
                Map<String, Object> avgDimensions = widgetRatingMapper.calAvgRating(blockWidgetAdminDTOs.get(i).getModuleWidgetAdminDTOs().get(j).getId(), day);
                if(avgDimensions==null){
                    avgDimensions = new HashMap<>();
                    avgDimensions.put("avgDimension1", 0.0);
                    avgDimensions.put("avgDimension2", 0.0);
                    avgDimensions.put("avgDimension3", 0.0);
                }

                blockWidgetAdminDTOs.get(i).getModuleWidgetAdminDTOs().get(j).setClickCount(clickCount);
                blockWidgetAdminDTOs.get(i).getModuleWidgetAdminDTOs().get(j).setAvgDimension1((double)avgDimensions.get("avgDimension1"));
                blockWidgetAdminDTOs.get(i).getModuleWidgetAdminDTOs().get(j).setAvgDimension2((double)avgDimensions.get("avgDimension2"));
                blockWidgetAdminDTOs.get(i).getModuleWidgetAdminDTOs().get(j).setAvgDimension3((double)avgDimensions.get("avgDimension3"));
            }
        }
        return blockWidgetAdminDTOs;
    }

    @Override
    @Transactional
    public int updateBlockOrder(List<Map<String, Object>> updateBlockList){
        for (int i = 0; i < updateBlockList.size(); i++) {
            blockWidgetMapper.updateBlockOrder((int) updateBlockList.get(i).get("id"), (int) updateBlockList.get(i).get("displayOrder"));
        }
        return 1;
    }

    @Override
    @Transactional
    public int addBlockWidget(BlockWidget blockWidget){
        int result = blockWidgetMapper.insertBlockWidget(blockWidget);
        if (result == 1) {
            return 1;   //添加成功
        } else {
            return 0;   // 添加失败
        }
    }

    @Override
    @Transactional
    public int updateBlockInfo(BlockWidget newBlockInfo){
        int result = blockWidgetMapper.updateBlockInfo(newBlockInfo);
        if (result == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    @Transactional
    public int updateModuleOrder(List<Map<String, Object>> updateModuleList){
        for (int i = 0; i < updateModuleList.size(); i++) {
            moduleWidgetMapper.updateModuleOrder((int) updateModuleList.get(i).get("id"), (int) updateModuleList.get(i).get("displayOrder"), (int) updateModuleList.get(i).get("fatherId"));
        }
        return 1;
    }

    @Override
    @Transactional
    public int addModuleWidget(ModuleWidget moduleWidget){
        int result = moduleWidgetMapper.insertModuleWidget(moduleWidget);
        if (result == 1) {
            return 1;   //添加成功
        } else {
            return 0;   // 添加失败
        }
    }

    @Override
    @Transactional
    public int updateModuleInfo(ModuleWidget newModuleInfo){
        int result = moduleWidgetMapper.updateModuleInfo(newModuleInfo);
        if (result == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    @Transactional
    public int updateHomePageInfo(HomePageInfo homePageInfo){
        int result = homePageInfoMapper.updateHomePageInfo(homePageInfo);
        if (result == 1) {
            return 1;
        } else {
            return 0;
        }
    }
}
