package com.hna;

import com.google.common.collect.Maps;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author owen
 */
public class DemoMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);
    private static final Integer MAX_RETRY_TIMES = 3;


    public static void main(String[] args) throws ParseException, InterruptedException {

        // 1.创建流程引擎
        ProcessEngine processEngine = getProcessEngine();

        // 2.部署流程定义文件
        ProcessDefinition processDefinition = getProcessDefinition(processEngine);

        // 3.启动运行流程
        ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);

        //4 处理流程任务
        Scanner scanner = new Scanner(System.in);
        int retry = 0;
        while (processInstance != null && !processInstance.isEnded()) {
            TaskService taskService = processEngine.getTaskService();
            List<Task> tasks = taskService.createTaskQuery().list();
            LOGGER.info("待处理任务数量 [{}]", tasks.size());
            if (tasks.isEmpty()) {
                if (retry < MAX_RETRY_TIMES) {
                    retry++;
                    Thread.sleep(10 * 1000);
                } else {
                    processInstance = null;
                }
            } else {
                retry = 0;
            }
            for (Task task : tasks) {
                LOGGER.info("待处理的任务 [{}]", task.getName());
                FormService formService = processEngine.getFormService();
                TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                List<FormProperty> formProperties = taskFormData.getFormProperties();
                Map<String, Object> variables = Maps.newHashMap();
                for (FormProperty formProperty : formProperties) {
                    String line = null;
                    if (StringFormType.class.isInstance(formProperty.getType())) {
                        LOGGER.info("请输入 {} ?", formProperty.getName());
                        line = scanner.nextLine();
                        variables.put(formProperty.getId(), line);
                    } else if (DateFormType.class.isInstance(formProperty.getType())) {
                        LOGGER.info("请输入 {} ? 格式（yyyy-MM-dd）", formProperty.getName());
                        line = scanner.nextLine();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date data = df.parse(line);
                        variables.put(formProperty.getId(), data);
                    } else {
                        LOGGER.info("类型暂不支持 {}", formProperty.getType());
                    }
                    LOGGER.info("您输入的内容时 [{}]", line);
                }
                taskService.complete(task.getId(), variables);
            }
        }

    }

    private static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        LOGGER.info("启动流程 [{}]", processInstance.getProcessDefinitionKey());
        return processInstance;
    }

    private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("二级审批流程.bpmn20.xml");
        Deployment deployment = deploymentBuilder.deploy();
        String deploymentId = deployment.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();
        LOGGER.info("流程定义文件 [{}]， 流程ID [{}]", processDefinition.getName(), processDefinition.getId());
        return processDefinition;
    }

    private static ProcessEngine getProcessEngine() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = cfg.buildProcessEngine();
        String name = processEngine.getName();
        String version = ProcessEngine.VERSION;
        LOGGER.info("流程引擎名称 [{}], 流程引擎版本 [{}]", name, version);
        return processEngine;
    }

}
