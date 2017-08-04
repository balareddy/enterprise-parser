package com.enterprise.parser.action;

import com.enterprise.parser.store.StoreManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

public abstract class AbstractOrderAction implements OrderAction {

    protected String message;
    protected StoreManager orderStoreManager;

    @Autowired
    public AbstractOrderAction(@NotNull StoreManager bookManager) {
        this.orderStoreManager = bookManager;
    }

    public AbstractOrderAction consume(@NotNull String message) {
        this.message = message;
        return this;
    }

    public abstract int qty();

    public abstract String orderId();

}
