package com.enterprise.parser.store;

import com.enterprise.parser.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.*;

@Component
public class OrderStoreManager implements StoreManager {

    private final int top;
    // Order Id <-> Order ( Open Orders )
    private final Map<String, Order> orderStore = new HashMap<>();
    // Stock Symbol <->  Order ( total executed orders )
    private final Map<String, Order> volumeStore = new HashMap<>();
    // Stock Symbol <->  Order ( top total executed orders )
    private final NavigableSet<Order> topVolumeStore = new TreeSet<>((o1, o2) -> {
        if (o1.getSymbol().equals(o2.getSymbol())) {
            return 0;
        }

        return Integer.compare(o2.getQty(), o1.getQty());
    });

    private static final String VOLUME_DISPLAY_FMT = "%-10s %-20d\n";

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStoreManager.class);

    public OrderStoreManager(@Value("${market.volume.top:10}") String top){
        this.top = Integer.parseInt(top);
    }

    @Override
    public Map<String, Order> getOrderStore() {
        return orderStore;
    }

    @Override
    public Map<String, Order> getVolumeStore() {
        return volumeStore;
    }

    @Override
    public Set<Order> getTopVolumeStore() {
        return topVolumeStore;
    }

    @Override
    public void updateTopVolumeStore(@NotNull String symbol) {
        if (volumeStore.containsKey(symbol)) {
            final Order order = volumeStore.get(symbol);

            while (topVolumeStore.size() >= top) {
                topVolumeStore.pollLast();
            }

            topVolumeStore.remove(order);
            topVolumeStore.add(order);
        }
    }

    public void printTopVolumeStore() {
        final StringBuilder topVolumeReport = topVolumeStore.stream()
                .map(order -> String.format(VOLUME_DISPLAY_FMT, order.getSymbol(), order.getQty()))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
        LOGGER.info(topVolumeReport.toString());
    }

    @Override
    public void reset() {
        orderStore.clear();
        volumeStore.clear();
        topVolumeStore.clear();
    }
}
