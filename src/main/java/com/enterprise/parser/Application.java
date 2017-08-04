package com.enterprise.parser;

import com.enterprise.parser.controller.ParserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) {
        if(args.length == 0){
            LOGGER.error("Please provide the file path as the argument");
            return;
        }
        final String fileName = args[0];
        final ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        final ParserController parserController = context.getBean(ParserController.class);
        parserController.parse(fileName);
        parserController.printTopVolumeStore();
    }
}
