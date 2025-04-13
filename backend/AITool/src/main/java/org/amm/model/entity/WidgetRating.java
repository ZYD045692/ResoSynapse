package org.amm.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class WidgetRating implements Serializable {
    private int id;
    private int widgetId;
    private int userId;
    private Timestamp ratingDate;
    private int dimension1;
    private int dimension2;
    private int dimension3;

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

    public Timestamp getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Timestamp ratingDate) {
        this.ratingDate = ratingDate;
    }

    public int getDimension1() {
        return dimension1;
    }

    public void setDimension1(int dimension1) {
        this.dimension1 = dimension1;
    }

    public int getDimension2() {
        return dimension2;
    }

    public void setDimension2(int dimension2) {
        this.dimension2 = dimension2;
    }

    public int getDimension3() {
        return dimension3;
    }

    public void setDimension3(int dimension3) {
        this.dimension3 = dimension3;
    }
}
