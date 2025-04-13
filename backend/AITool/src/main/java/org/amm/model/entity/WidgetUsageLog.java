package org.amm.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class WidgetUsageLog implements Serializable{
    private int id;
    private int widgetId;
    private int userId;
    private Timestamp logDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getLogDate() {
        return logDate;
    }

    public void setLogDate(Timestamp logDate) {
        this.logDate = logDate;
    }
}
