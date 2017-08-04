package com.enterprise.parser.factory;

import com.enterprise.parser.action.AddOrderAction;
import com.enterprise.parser.action.CancelOrderAction;
import com.enterprise.parser.action.ExecutionOrderAction;
import com.enterprise.parser.action.TradeOrderAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class OrderActionFactory {

    private static final char ORDER_ADD = 'A';
    private static final char ORDER_CANCEL = 'X';
    private static final char ORDER_EXECUTION = 'E';
    private static final char ORDER_TRADE = 'P';
    private static final char IGNORE_CHAR = 'S';
    private final AddOrderAction addOrderAction;
    private final CancelOrderAction cancelOrderAction;
    private final ExecutionOrderAction executedOrderAction;
    private final TradeOrderAction tradeOrderAction;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderActionFactory.class);

    @Autowired
    public OrderActionFactory(@NotNull AddOrderAction addOrderAction, @NotNull CancelOrderAction cancelOrderAction,
                              @NotNull ExecutionOrderAction executedOrderAction, @NotNull TradeOrderAction tradeOrderAction) {
        this.addOrderAction = addOrderAction;
        this.cancelOrderAction = cancelOrderAction;
        this.executedOrderAction = executedOrderAction;
        this.tradeOrderAction = tradeOrderAction;
    }

    public void execute(@NotNull String message) {
        if (message.charAt(0) == IGNORE_CHAR) {
            message = message.substring(1);
        }

        char messageType = message.charAt(8);

        switch (messageType) {
            case ORDER_ADD:
                addOrderAction.consume(message).execute();
                break;
            case ORDER_EXECUTION:
                executedOrderAction.consume(message).execute();
                break;
            case ORDER_CANCEL:
                cancelOrderAction.consume(message).execute();
                break;
            case ORDER_TRADE:
                tradeOrderAction.consume(message).execute();
                break;
            default:
                LOGGER.warn("unprocessed message {}", message);
        }
    }
}
