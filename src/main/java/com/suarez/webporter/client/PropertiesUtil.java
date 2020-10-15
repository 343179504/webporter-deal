package com.suarez.webporter.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 配置
 * @author adao
 */
@Component
public class PropertiesUtil {
    private final static Logger logger = Logger.getLogger(PropertiesUtil.class);

    public static final String propertiesPath = "src/main/resources/constant.properties";

    private static Constant constant;

    @Autowired
    public void setConfig(Constant constant) {
        PropertiesUtil.constant = constant;
    }

    public static boolean getConTaskEnable() {
        return constant.isTaskEnable();
    }

    public static void setConTaskEnable(boolean taskEnable) {
        constant.setTaskEnable(taskEnable);
    }

    public static String getConServerAddress() {
        return constant.getServerAddress();
    }

    public static void setConServerAddress(String serverAddress) {
        constant.setServerAddress(serverAddress);
    }

    public static Integer getConServerPort() {
        return constant.getServerPort();
    }

    public static void setConServerPort(Integer serverPort) {
        constant.setServerPort(serverPort);
    }

    public static Long getConEventPeriodUnit() {
        return constant.getEventPeriodUnit();
    }

    public static void setConEventPeriodUnit(Long eventPeriodUnit) {
        constant.setEventPeriodUnit(eventPeriodUnit);
    }

    public static Integer getConEventPeriodMax() {
        return constant.getEventPeriodMax();
    }

    public static void setConEventPeriodMax(int eventPeriodMax) {
        constant.setEventPeriodMax(eventPeriodMax);
    }

    public static Integer getConEventPeriodMin() {
        return constant.getEventPeriodMin();
    }

    public static void setConEventPeriodMin(Integer eventPeriodMin) {
        constant.setEventPeriodMin(eventPeriodMin);
    }

    public static String getConUrl() {
        return constant.getUrl();
    }

    public static void setConUrl(String url) {
        constant.setUrl(url);
    }

    public static String getConUserName() {
        return constant.getUserName();
    }

    public static void setConUserName(String userName) {
        constant.setUserName(userName);
    }

    public static String getConPassWord() {
        return constant.getPassWord();
    }

    public static void setConPassWord(String passWord) {
        constant.setPassWord(passWord);
    }

    public static boolean getConHeartEnable() {
        return constant.isHeartEnable();
    }


}
