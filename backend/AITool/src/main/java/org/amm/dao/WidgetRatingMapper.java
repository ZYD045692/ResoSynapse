package org.amm.dao;

import org.amm.model.entity.WidgetRating;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface WidgetRatingMapper {
    //添加用户评价
    public int insertWidgetRating(WidgetRating widgetRating);

    //计算某天距今天内某个小组件的平均rating
    public Map<String, Object> calAvgRating(@Param("widgetId") int widgetId, @Param("day") int day);
}
