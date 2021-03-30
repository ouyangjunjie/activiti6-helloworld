package com.hna.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author owen
 */
public class MDCErrorDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MDCErrorDelegate.class);

    public void execute(DelegateExecution delegateExecution) {
        LOGGER.info("run MDCErrorDelegate");
        throw new RuntimeException("only test MDC");
    }
}
