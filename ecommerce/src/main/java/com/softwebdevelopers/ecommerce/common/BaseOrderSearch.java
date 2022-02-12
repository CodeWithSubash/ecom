package com.softwebdevelopers.ecommerce.common;

public abstract class BaseOrderSearch extends BasePage {

    private String OrderType;

    private String orderBy;

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getOrderBy() {
        if(this.orderBy != null && !this.orderBy.isEmpty()) {
            return this.orderBy + " " + this.OrderType;
        }
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
