package com.rookies4.myspringbootlab.runner;

import com.rookies4.myspringbootlab.property.MyPropProperties;
import com.rookies4.myspringbootlab.config.vo.MyEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    private final MyPropProperties myPropProperties;
    private final MyEnvironment myEnvironment;

    @Autowired
    public MyPropRunner(MyPropProperties myPropProperties, MyEnvironment myEnvironment) {
        this.myPropProperties = myPropProperties;
        this.myEnvironment = myEnvironment;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("========================================");
        logger.info("Active Profile Mode: {}", myEnvironment.getMode());
        logger.info("[Props Class] myprop.username: {}", myPropProperties.getUsername());
        logger.info("[Props Class] myprop.port: {}", myPropProperties.getPort());
        logger.debug("This is a DEBUG level message for testing.");
        logger.info("========================================");
    }
}