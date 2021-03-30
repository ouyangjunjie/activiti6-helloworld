package com.hna.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author owen
 */
public class CustomEventListener implements ActivitiEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomEventListener.class);

    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        if (ActivitiEventType.CUSTOM == eventType) {
            LOGGER.info("监听到自定义事件 {} \t {}", eventType, event.getProcessInstanceId());
        }
    }

    public boolean isFailOnException() {
        return false;
    }
}
