package com.cat.engine.core.service.config;

import com.cat.engine.core.constant.EngineConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author alex5leo7
 * 项目配置信息
 */
@ConfigurationProperties(prefix = EngineConstant.PROPERTIES_PREFIX)
@Configuration
@Data
public class EngineConfigProperties {
    /**
     * 是否使用规则引擎
     */
    private Boolean enabled;

    /**
     * 主配置名
     */
    private String configName;
}
