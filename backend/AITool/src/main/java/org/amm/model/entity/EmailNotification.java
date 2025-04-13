package org.amm.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class EmailNotification implements Serializable {
    private String name;
    private String widgetUrl;
    private int blockId;
    private String iconUrl;
    private Timestamp creationDate;
    private int isHandled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWidgetUrl() {
        return widgetUrl;
    }

    public void setWidgetUrl(String widgetUrl) {
        this.widgetUrl = widgetUrl;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public int getIsHandled() {
        return isHandled;
    }

    public void setIsHandled(int isHandled) {
        this.isHandled = isHandled;
    }
}
