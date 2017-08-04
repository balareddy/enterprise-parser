package com.enterprise.parser.controller;

import com.enterprise.parser.service.OrderActionService;
import com.enterprise.parser.store.StoreManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class ParserController {

    private final OrderActionService orderActionService;
    private final StoreManager orderStoreManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(ParserController.class);

    @Autowired
    public ParserController(@NotNull OrderActionService orderActionService, @NotNull StoreManager bookManager) {
        this.orderActionService = orderActionService;
        this.orderStoreManager = bookManager;
    }

    public void parse(String filePath) {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(orderActionService::execute);
        } catch (IOException e) {
            LOGGER.error("failed processing the filePath {}", filePath, e);
        }
    }

    public void printTopVolumeStore() {
        orderStoreManager.printTopVolumeStore();
    }
}
