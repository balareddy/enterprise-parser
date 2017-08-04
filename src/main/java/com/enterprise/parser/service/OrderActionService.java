package com.enterprise.parser.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.enterprise.parser.factory.OrderActionFactory;

import javax.validation.constraints.NotNull;

@Service
public class OrderActionService {

    private final OrderActionFactory orderActionFactory;

    @Autowired
    public OrderActionService(@NotNull OrderActionFactory orderActionFactory) {
        this.orderActionFactory = orderActionFactory;
    }

    public void execute(String message) {
        this.orderActionFactory.execute(message);
    }

}
