package com.enterprise.parser.store;

import com.enterprise.parser.model.Order;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public interface StoreManager {
    Map<String, Order> getOrderStore();

    Map<String, Order> getVolumeStore();

    Set<Order> getTopVolumeStore();

    void updateTopVolumeStore(@NotNull String symbol);

    void printTopVolumeStore();

    void reset();
}
