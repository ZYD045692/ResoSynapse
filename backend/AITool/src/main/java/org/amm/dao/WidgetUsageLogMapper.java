package org.amm.dao;

import org.amm.model.entity.WidgetUsageLog;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface WidgetUsageLogMapper {
    // 插入使用日志
    public int insertWidgetUsageLog(WidgetUsageLog widgetUsageLog);

    // 计算据今天多少天内某个小组件的点击次数
    public int calClickCount(@Param("widgetId") int widgetId, @Param("day") int day);

}
