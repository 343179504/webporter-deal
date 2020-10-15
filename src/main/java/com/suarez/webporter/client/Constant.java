package com.suarez.webporter.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 **/
@Configuration
@ConfigurationProperties(prefix = "constant")
@PropertySource(value = "classpath:constant.properties")
public class Constant {

    private boolean taskEnable;
    private Long taskDelay;
    private boolean heartEnable;
    private Long clientHeart;
    private String serverAddress;
    private Integer serverPort;
    private Integer connectTimeout;
    private Long eventPeriodUnit;
    private Integer eventPeriodMax;
    private Integer eventPeriodMin;
    private String url;
    private String userName;
    private String passWord;

    public boolean isTaskEnable() {
        return taskEnable;
    }

    public void setTaskEnable(boolean taskEnable) {
        this.taskEnable = taskEnable;
    }

    public Long getTaskDelay() {
        return taskDelay;
    }

    public void setTaskDelay(Long taskDelay) {
        this.taskDelay = taskDelay;
    }

    public boolean isHeartEnable() {
        return heartEnable;
    }

    public void setHeartEnable(boolean heartEnable) {
        this.heartEnable = heartEnable;
    }

    public Long getClientHeart() {
        return clientHeart;
    }

    public void setClientHeart(Long clientHeart) {
        this.clientHeart = clientHeart;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Long getEventPeriodUnit() {
        return eventPeriodUnit;
    }

    public void setEventPeriodUnit(Long eventPeriodUnit) {
        this.eventPeriodUnit = eventPeriodUnit;
    }

    public Integer getEventPeriodMax() {
        return eventPeriodMax;
    }

    public void setEventPeriodMax(Integer eventPeriodMax) {
        this.eventPeriodMax = eventPeriodMax;
    }

    public Integer getEventPeriodMin() {
        return eventPeriodMin;
    }

    public void setEventPeriodMin(Integer eventPeriodMin) {
        this.eventPeriodMin = eventPeriodMin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}