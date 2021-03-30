package com.hna;

import com.google.common.collect.Maps;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * history_level
 */
public class ConfigHistoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHistoryTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_history.cfg.xml");

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testConfig1() {
        // 1.带参数启动流程
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("keyStart1", "value1");
        variables.put("keyStart2", "value2");
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        assertNotNull(processInstance);

        // 2.修改变量
        List<Execution> executions = activitiRule.getRuntimeService().createExecutionQuery()
                .processInstanceId(processInstance.getId())
                .listPage(0, 100);
        for (Execution execution : executions) {
            LOGGER.info("execute = {}", execution);
        }
        LOGGER.info("execute size = {}", executions.size());
        String id = executions.iterator().next().getId();
        activitiRule.getRuntimeService().setVariable(id, "keyStart1", "value1_");

        // 3.提交表单 task
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Map<String, String> formData = Maps.newHashMap();
        formData.put("formKey1", "valuef1");
        formData.put("formKey2", "valuef2");
        activitiRule.getFormService().submitTaskFormData(task.getId(), formData);

        // 输出历史内容
        // 输出历史活动
        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService()
                .createHistoricActivityInstanceQuery().listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info("historicActivityInstance = {}", historicActivityInstance);
        }
        LOGGER.info("historicActivityInstances size = {}", historicActivityInstances.size());

        List<HistoricVariableInstance> historicVariableInstances = activitiRule.getHistoryService()
                .createHistoricVariableInstanceQuery().listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("historicVariableInstance = {}", historicVariableInstance);
        }
        LOGGER.info("historicVariableInstances size = {}", historicVariableInstances.size());

        // 输出历史表单
        List<HistoricTaskInstance> historicTaskInstances = activitiRule.getHistoryService()
                .createHistoricTaskInstanceQuery().listPage(0, 100);
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            LOGGER.info("historicTaskInstance = {}", historicTaskInstance);
        }
        LOGGER.info("historicTaskInstances size = {}", historicTaskInstances.size());

        List<HistoricDetail> historicDetailsForm = activitiRule.getHistoryService()
                .createHistoricDetailQuery()
                .formProperties()
                .listPage(0, 100);
        for (HistoricDetail historicDetailForm : historicDetailsForm) {
            LOGGER.info("historicDetailForm = {}", toString(historicDetailForm));
        }
        LOGGER.info("historicDetailsForm size = {}", historicDetailsForm.size());

        // 输出历史详情
        List<HistoricDetail> historicDetails = activitiRule.getHistoryService()
                .createHistoricDetailQuery()
                .listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            LOGGER.info("historicDetail = {}", toString(historicDetail));
        }
        LOGGER.info("historicDetails size = {}", historicDetails.size());
    }

    static String toString(HistoricDetail detail) {
        return ToStringBuilder.reflectionToString(detail, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
