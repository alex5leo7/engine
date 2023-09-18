package com.cat.engine.starter.autoconfigure;

import com.cat.engine.core.constant.EngineConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * engine Nacos 自动配置类
 *
 * @author alex5leo7
 */
@Configuration
@ConditionalOnClass(value = com.alibaba.nacos.api.config.ConfigService.class)
@ConditionalOnProperty(value = EngineConstant.ENGINE_ENABLED_PROPERTIES, havingValue = "true")
@ComponentScan(EngineConstant.SCAN_PATH)
public class EngineAutoConfiguration {

}
