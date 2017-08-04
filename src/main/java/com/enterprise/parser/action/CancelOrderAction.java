package com.enterprise.parser.action;

import com.enterprise.parser.model.Order;
import com.enterprise.parser.store.StoreManager;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class CancelOrderAction extends AbstractOrderAction {

    public CancelOrderAction(@NotNull StoreManager bookManager) {
        super(bookManager);
    }

    @Override
    public int qty() {
        return Integer.valueOf(message.substring(21, 27));
    }

    @Override
    public String orderId() {
        return message.substring(9, 21);
    }

    @Override
    public void execute() {
        if (orderStoreManager.getOrderStore().containsKey(this.orderId())) {
            final Order orderData = orderStoreManager.getOrderStore().get(this.orderId());
            final int qty = this.qty();
            if (qty <= orderData.getQty()) {
                orderData.updateExecutions(qty);
                orderStoreManager.getVolumeStore().computeIfPresent(orderData.getSymbol(), (symbolName, ord) -> ord.updateExecutions(-1 * qty));
                orderStoreManager.updateTopVolumeStore(orderData.getSymbol());
                if (orderData.getQty() == 0) {
                    orderStoreManager.getOrderStore().remove(this.orderId());
                }
            }
        }
    }
}
