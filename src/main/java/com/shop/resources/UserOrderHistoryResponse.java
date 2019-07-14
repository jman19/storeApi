package com.shop.resources;

import com.shop.data.impl.Fulfillment;

import java.util.List;

public class UserOrderHistoryResponse {
    private List<Fulfillment> fulfillment;

    public UserOrderHistoryResponse(List<Fulfillment> fulfillment) {
        this.fulfillment = fulfillment;
    }

    public List<Fulfillment> getFulfillment() {
        return fulfillment;
    }

    public void setFulfillment(List<Fulfillment> fulfillment) {
        this.fulfillment = fulfillment;
    }
}
