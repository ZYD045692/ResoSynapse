package org.amm.model.dto;

import java.io.Serializable;
import java.util.List;
import org.amm.model.entity.ModuleWidget;

public class BlockWidgetAdminDTO implements Serializable {
    private int id;
    private String name;
    private String iconUrl;
    private int displayOrder;
    private int isVisible;
    private String imgUrl;
    private String introduction;
    private List<ModuleWidgetAdminDTO> moduleWidgetAdminDTOs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public List<ModuleWidgetAdminDTO> getModuleWidgetAdminDTOs() {
        return moduleWidgetAdminDTOs;
    }

    public void setModuleWidgetAdminDTOs(List<ModuleWidgetAdminDTO> moduleWidgetAdminDTOs) {
        this.moduleWidgetAdminDTOs = moduleWidgetAdminDTOs;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
