package com.hna.interceptor;

import org.activiti.engine.debug.ExecutionTreeUtil;
import org.activiti.engine.impl.agenda.AbstractOperation;
import org.activiti.engine.impl.interceptor.DebugCommandInvoker;
import org.activiti.engine.logging.LogMDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author owen
 */
public class MDCCommandInvoker extends DebugCommandInvoker {

    private static final Logger LOGGER = LoggerFactory.getLogger(MDCCommandInvoker.class);

    @Override
    public void executeOperation(Runnable runnable) {
        boolean mdcEnable = LogMDC.isMDCEnabled();
        LogMDC.setMDCEnabled(true);
        if (runnable instanceof AbstractOperation) {
            AbstractOperation operation = (AbstractOperation)runnable;
            if (operation.getExecution() != null) {
                LogMDC.putMDCExecution(operation.getExecution());
            }
        }
        super.executeOperation(runnable);
        LogMDC.clear();
        if (!mdcEnable) {
            LogMDC.setMDCEnabled(false);
        }
    }

}
