package com.hna;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigDBTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDBTest.class);

    @Test
    public void testConfig1() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault();
        LOGGER.info("Configuration = {}", configuration);

        ProcessEngine processEngine = configuration.buildProcessEngine();
        LOGGER.info("获取流程引擎 {}", processEngine.getName());
        processEngine.close();
    }

    @Test
    public void testConfig2() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");
        LOGGER.info("Configuration = {}", configuration);

        ProcessEngine processEngine = configuration.buildProcessEngine();
        LOGGER.info("获取流程引擎 {}", processEngine.getName());
        processEngine.close();
    }

    @Test
    public void testConfig3() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti_hikar.cfg.xml");
        LOGGER.info("Configuration = {}", configuration);

        ProcessEngine processEngine = configuration.buildProcessEngine();
        LOGGER.info("获取流程引擎 {}", processEngine.getName());
        processEngine.close();
    }

}
