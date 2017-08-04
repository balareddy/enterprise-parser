package com.enterprise.parser.action;

import com.enterprise.parser.model.Order;
import com.enterprise.parser.store.StoreManager;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class AddOrderAction extends AbstractOrderAction {

    public AddOrderAction(@NotNull StoreManager bookManager) {
        super(bookManager);
    }

    public String symbol() {
        return message.substring(28, 34).trim();
    }

    @Override
    public int qty() {
        return Integer.parseInt(message.substring(22, 28));
    }

    @Override
    public String orderId() {
        return message.substring(9, 21);
    }

    @Override
    public void execute() {
        final String symbol = this.symbol();
        final String orderId = this.orderId();
        final int orderQty = this.qty();

        final Order order = new Order(symbol, orderQty);
        orderStoreManager.getOrderStore().put(orderId, order);
        orderStoreManager.getVolumeStore().computeIfAbsent(symbol, symbolName -> new Order(symbolName, orderQty));
        orderStoreManager.updateTopVolumeStore(symbol);
    }
}
