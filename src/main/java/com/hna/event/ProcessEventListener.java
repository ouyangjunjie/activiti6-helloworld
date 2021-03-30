package com.hna.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author owen
 */
public class ProcessEventListener implements ActivitiEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessEventListener.class);

    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        if (ActivitiEventType.PROCESS_STARTED == eventType) {
            LOGGER.info("流程启动 {} \t {}", eventType, event.getProcessInstanceId());
        } else if (ActivitiEventType.PROCESS_CANCELLED == eventType) {
            LOGGER.info("流程结束 {} \t {}", eventType, event.getProcessInstanceId());
        }
    }

    public boolean isFailOnException() {
        return false;
    }
}
