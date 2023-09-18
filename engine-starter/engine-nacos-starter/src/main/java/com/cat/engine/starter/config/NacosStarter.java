package com.cat.engine.starter.config;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.base.Throwables;
import com.cat.engine.core.constant.EngineConstant;
import com.cat.engine.core.service.bootstrap.BaseEngineBaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;


/**
 * @author alex5leo7
 * nacos 启动器
 */
@Service
@Slf4j
public class NacosStarter extends BaseEngineBaseConfig implements Listener {

    @NacosInjected
    private ConfigService configService;

    @Override
    public void addListener() {
        try {
            configService.addListener(configProperties.getConfigName(), EngineConstant.NACOS_DEFAULT_GROUP, this);
            log.info("分布式配置中心配置[{}]监听器已启动", configProperties.getConfigName());
        } catch (Exception e) {
            log.error("EngineConfigService#refresh key:[{}] fail:{}", configProperties.getConfigName(), Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public String getConfigValueByName(String configName) {
        try {
            return configService.getConfig(configName, EngineConstant.NACOS_DEFAULT_GROUP, 3000L);
        } catch (NacosException e) {
            log.error("EngineConfigService#getConfigValueByName key:[{}],fail:{}", configName, Throwables.getStackTraceAsString(e));
        }
        return null;
    }

    @Override
    public void receiveConfigInfo(String mainConfig) {
        log.info("分布式配置中心监听到[{}]数据更新:{}", configProperties.getConfigName(), mainConfig);
        bootstrap(mainConfig);
    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void addOrUpdateProperty(String key, String value) {

    }

    @Override
    public void removeProperty(String key) {
        try {
            configService.removeConfig(key, EngineConstant.NACOS_DEFAULT_GROUP);
        } catch (NacosException e) {
            log.error("EngineConfigService#removeProperty fail:{}", Throwables.getStackTraceAsString(e));
        }
    }

}
