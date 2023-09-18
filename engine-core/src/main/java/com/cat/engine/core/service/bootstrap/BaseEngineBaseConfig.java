package com.cat.engine.core.service.bootstrap;

import com.alibaba.fastjson2.JSON;
import com.cat.engine.core.domain.MainConfig;
import com.cat.engine.core.service.bean.RegisterBeanService;
import com.cat.engine.core.service.config.EngineConfigProperties;
import com.cat.engine.core.utils.EngineCache;
import com.cat.engine.core.utils.GroovyUtils;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
public abstract class BaseEngineBaseConfig implements EngineBaseConfig {
    @Autowired
    protected EngineConfigProperties configProperties;

    @Autowired
    private RegisterBeanService registerBeanService;

    @PostConstruct
    public void init() {
        // 创建出默认主配置
        if (!StringUtils.hasText(getConfigValueByName(configProperties.getConfigName()))) {
            MainConfig initConfig = MainConfig.builder().instanceNames(new ArrayList<>()).updateTime(System.currentTimeMillis()).build();
            addOrUpdateProperty(configProperties.getConfigName(), JSON.toJSONString(initConfig));
        }
        // 启动解析并注册监听器
        if (StringUtils.hasText(getConfigValueByName(configProperties.getConfigName()))) {
            bootstrap(getConfigValueByName(configProperties.getConfigName()));
            addListener();
        }
    }

    /**
     * 1、解析主配置
     * 2、得到每个groovy配置并比对有无变化
     * 3、有变化的groovy配置重新注册
     *
     * @param mainConfig {"instanceNames":["com.cat.engine.core.constant.EngineConstant"],"updateTime":"2023年3月20日10:26:0131"}
     */
    public synchronized void bootstrap(String mainConfig) {
        try {
            MainConfig config = JSON.parseObject(mainConfig, MainConfig.class);
            for (String instanceName : config.getInstanceNames()) {
                String groovyCode = getConfigValueByName(instanceName);
                if (StringUtils.hasText(groovyCode) && EngineCache.diff(instanceName, groovyCode)) {
                    register(instanceName, groovyCode);
                }
            }
        } catch (Exception e) {
            log.error("parseConfigAndRegister fail! config:{},exception:{}", mainConfig, Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 1、调用groovy加载器解析，生成class对象
     * 2、class对象注册到Spring IOC 容器中
     *
     * @param instanceName
     * @param groovyCode
     */
    public void register(String instanceName, String groovyCode) {
        Class clazz = GroovyUtils.parseClass(instanceName, groovyCode);
        if (Objects.nonNull(clazz)) {
            Object bean = registerBeanService.registerBean(instanceName, clazz);
            log.info("bean:[{}]已注册到Spring IOC中", bean.getClass().getName());
        }
    }
}
