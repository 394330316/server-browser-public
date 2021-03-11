package com.browser.config;

import com.browser.data.ChannelControl;
import com.browser.data.Website;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "browserconfig")
public class BrowserConfig {

    private List<ChannelControl> channelControl = new ArrayList<>();

    private List<Website> websites = new ArrayList<>();

    private List<Website> luckymoney = new ArrayList<>();

    public List<ChannelControl> getChannelControl() {
        return channelControl;
    }

    public void setChannelControl(List<ChannelControl> channelControl) {
        this.channelControl = channelControl;
    }

    public List<Website> getWebsites() {
        return websites;
    }

    public void setWebsites(List<Website> websites) {
        this.websites = websites;
    }

    public List<Website> getLuckymoney() {
        return luckymoney;
    }

    public void setLuckymoney(List<Website> luckymoney) {
        this.luckymoney = luckymoney;
    }
}
