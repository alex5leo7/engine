package com.cat.engine.core.service.bootstrap;


/**
 * 配置服务
 */
public interface EngineBaseConfig {

    /**
     * 启动配置变更监听器
     */
    void addListener();

    /**
     * 通过文件名获取得到文件内容
     *
     * @param configName
     * @return
     */
    String getConfigValueByName(String configName);

    /**
     * 设置配置(nacos)
     *
     * @param key
     * @param value
     */
    void addOrUpdateProperty(String key, String value);

    /**
     * 删除配置(nacos)
     * @param key
     */
    void removeProperty(String key);
}
