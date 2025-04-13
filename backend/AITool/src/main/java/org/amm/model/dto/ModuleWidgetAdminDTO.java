package org.amm.model.dto;

import java.io.Serializable;

public class ModuleWidgetAdminDTO implements Serializable {
    private int id;
    private String name;
    private String url;
    private String iconUrl;
    private int displayOrder;
    private int isVisible;
    private int fatherId;
    private String introduction;
    private double avgDimension1;
    private double avgDimension2;
    private double avgDimension3;
    private int clickCount;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public int getFatherId() {
        return fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public double getAvgDimension1() {
        return avgDimension1;
    }

    public void setAvgDimension1(double avgDimension1) {
        this.avgDimension1 = avgDimension1;
    }

    public double getAvgDimension2() {
        return avgDimension2;
    }

    public void setAvgDimension2(double avgDimension2) {
        this.avgDimension2 = avgDimension2;
    }

    public double getAvgDimension3() {
        return avgDimension3;
    }

    public void setAvgDimension3(double avgDimension3) {
        this.avgDimension3 = avgDimension3;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
}
