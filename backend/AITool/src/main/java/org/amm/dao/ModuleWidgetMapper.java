package org.amm.dao;

import java.util.List;
import java.util.Map;

import org.amm.model.entity.BlockWidget;
import org.amm.model.entity.ModuleWidget;
import org.apache.ibatis.annotations.Param;

public interface ModuleWidgetMapper {
    //查询所有能看见的小组件
    public List<ModuleWidget> getVisibleModuleWidget();

    // 修改小组件顺序
    public int updateModuleOrder(@Param("id") int id, @Param("displayOrder") int displayOrder, @Param("fatherId") int fatherId);

    //模块添加
    public int insertModuleWidget(ModuleWidget moduleWidget);

    //模块信息修改
    public int updateModuleInfo(ModuleWidget newModuleInfo);


}
