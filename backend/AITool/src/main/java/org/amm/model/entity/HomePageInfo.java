package org.amm.model.entity;

import java.io.Serializable;
import java.util.Map;

public class HomePageInfo implements Serializable{
    private int id;
    private String pageName;
    private String topNavigationText1;
    private String topNavigationText2;
    private String backgroundUrl;
    private String applyUrlText;
    private String feedbackText;
    private String dim1;
    private String dim2;
    private String dim3;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getTopNavigationText1() {
        return topNavigationText1;
    }

    public void setTopNavigationText1(String topNavigationText1) {
        this.topNavigationText1 = topNavigationText1;
    }

    public String getTopNavigationText2() {
        return topNavigationText2;
    }

    public void setTopNavigationText2(String topNavigationText2) {
        this.topNavigationText2 = topNavigationText2;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getApplyUrlText() {
        return applyUrlText;
    }

    public void setApplyUrlText(String applyUrlText) {
        this.applyUrlText = applyUrlText;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public String getDim1() {
        return dim1;
    }

    public void setDim1(String dim1) {
        this.dim1 = dim1;
    }

    public String getDim2() {
        return dim2;
    }

    public void setDim2(String dim2) {
        this.dim2 = dim2;
    }

    public String getDim3() {
        return dim3;
    }

    public void setDim3(String dim3) {
        this.dim3 = dim3;
    }
}
