package com.browser.controller;

import com.browser.config.LuckyMoneyConfig;
import com.browser.config.BrowserConfig;
import com.browser.config.UpgradeConfig;
import com.browser.service.BrowserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/browser")
public class BrowserController {

    @Autowired(required = false)
    private LuckyMoneyConfig luckyMoneyConfig;

    @Autowired(required = false)
    private BrowserConfig browserConfig;

    @Autowired(required = false)
    private BrowserService browserService;

    @RequestMapping({"/showTitle", "/show"})
    public String showTitle(@RequestParam(value = "id", required = false, defaultValue = "-1") Integer id) {
        return "认知" + "luckyMoneyConfig:" + luckyMoneyConfig.getUrl() + " id:"+ id;
    }

    @GetMapping("/getBrowserConfig")
    public BrowserConfig getBrowserConfig(@RequestParam("channel") String channel) {
        return browserConfig;
    }

    @RequestMapping("/getUpgradeUrl")
    public UpgradeConfig getUpgradeUrl(@RequestParam("versionCode") Integer versionCode) {
        return browserService.getUpgradeConfig(versionCode);
    }
}
