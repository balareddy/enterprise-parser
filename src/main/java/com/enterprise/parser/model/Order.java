package com.enterprise.parser.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;

public class Order {
    public String symbol;
    public int qty;

    public Order(@NotNull String symbol, int qty) {
        this.symbol = symbol;
        this.qty = qty;
    }

    public Order updateExecutions(int qty) {
        this.qty -= qty;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQty() {
        return qty;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("symbol", symbol)
                .add("qty", qty)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        final Order other = (Order) o;
        return Objects.equal(this.symbol, other.symbol)
                && Objects.equal(this.qty, other.qty);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.symbol, this.qty);
    }
}
