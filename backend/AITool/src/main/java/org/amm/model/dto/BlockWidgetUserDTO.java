package org.amm.model.dto;

import java.util.List;

public class BlockWidgetUserDTO {
    private int id;
    private String name;
    private String iconUrl;
    private int displayOrder;
    private int isVisible;
    private List<ModuleWidgetUserDTO> moduleWidgetUserDTOs;

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

    public List<ModuleWidgetUserDTO> getModuleWidgetUserDTOs() {
        return moduleWidgetUserDTOs;
    }

    public void setModuleWidgetUserDTOs(List<ModuleWidgetUserDTO> moduleWidgetUserDTOs) {
        this.moduleWidgetUserDTOs = moduleWidgetUserDTOs;
    }
}
