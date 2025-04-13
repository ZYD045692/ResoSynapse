package org.amm.dao;

import org.amm.model.dto.BlockWidgetAdminDTO;
import org.amm.model.dto.BlockWidgetUserDTO;
import org.amm.model.entity.BlockWidget;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BlockWidgetMapper {

    // 查询所有可见大组件及其小组件对应关系(用户视图）
    public List<BlockWidgetUserDTO> getUserWidgetMap();

    //查询所有大组件及其小组件对应关系  （管理员视图）is_visible=false放后面
    public List<BlockWidgetAdminDTO> getAdminWidgetMap();

    // 板块顺序修改
    public int updateBlockOrder(@Param("id") int id, @Param("displayOrder") int displayOrder);

    // 板块添加
    public int insertBlockWidget(BlockWidget blockWidget);

    // 板块信息修改
    public int updateBlockInfo(BlockWidget newBlockInfo);

}
