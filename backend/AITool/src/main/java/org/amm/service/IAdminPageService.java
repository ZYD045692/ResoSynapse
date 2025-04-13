package org.amm.service;

import org.amm.model.dto.BlockWidgetAdminDTO;
import org.amm.model.entity.BlockWidget;
import org.amm.model.entity.HomePageInfo;
import org.amm.model.entity.ModuleWidget;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface IAdminPageService {
    //返回所有板块
    List<BlockWidgetAdminDTO> getAdminBlocks(int day);

    //修改板块顺序（批量）
    @Transactional
    int updateBlockOrder(List<Map<String, Object>> updateBlockList);

    // 板块添加
    @Transactional
    int addBlockWidget(BlockWidget blockWidget);

    //修改板块信息
    @Transactional
    int updateBlockInfo(BlockWidget newBlockInfo);

    // 修改模块顺序（批量）
    @Transactional
    int updateModuleOrder(List<Map<String, Object>> updateModuleList);

    // 模块添加
    int addModuleWidget(ModuleWidget moduleWidget);

    //修改模块信息
    int updateModuleInfo(ModuleWidget newModuleInfo);

    // 修改homepage信息
    int updateHomePageInfo(HomePageInfo homePageInfo);

}
