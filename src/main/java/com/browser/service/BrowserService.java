package com.browser.service;

import com.browser.config.UpgradeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrowserService {

    @Autowired
    private UpgradeConfig upgradeConfig;

    /**
     * 查询是否有更新
     *
     * @param versionCode
     * @return
     */
    public UpgradeConfig getUpgradeConfig(int versionCode) {
        // 如果参数versionCode小于当前配置，返回服务器的数据
        if (versionCode < upgradeConfig.getVersionCode()) {
            return upgradeConfig;
        }
        return null;
    }


}
