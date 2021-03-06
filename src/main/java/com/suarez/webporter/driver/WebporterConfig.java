package com.suarez.webporter.driver;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class WebporterConfig {


    @Value("${webdriver.chrome.driver.86.address}")
    private String webdriverAddresses;

    @Value("${webdriver.chrome.listen.address}")
    private String listenAddress;

    @Value("${webdriver.chrome.listen.address.bet}")
    private String bet_listenAddress;

    @Value("${webdriver.chrome.listen.address.nwb}")
    private String nwb_listenAddress;

    @Value("${webdriver.chrome.listen.address.yzty}")
    private String yzty_listenAddress;

    @Value("${webdriver.chrome.listen.address.imty}")
    private String imty_listenAddress;
}
