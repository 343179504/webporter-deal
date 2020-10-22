package com.suarez.webporter.driver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;

@Slf4j
@Component
public class WbDriver {
    @Autowired
    private WebporterConfig webporterConfig;

    private WebDriver driver;
    private Actions action;
    public void init(){
        System.setProperty("webdriver.chrome.driver", webporterConfig.getWebdriverAddresses());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.setExperimentalOption("debuggerAddress", webporterConfig.getListenAddress());
        driver = new ChromeDriver(options);
        action = new Actions(driver);
        driver.switchTo().frame("sb_frame");


    }
    public void setAttribuate(WebElement eleemnt, String attrName, String attrValue){


        ((JavascriptExecutor)driver).executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", eleemnt,attrName,attrValue);

    }

    public void removeAttribuate(WebElement eleemnt,String attrName,String attrValue)
    {
        ((JavascriptExecutor)driver).executeScript("arguments[0].removeAttribute(arguments[1],arguments[2])", eleemnt,attrName,attrValue);

    }
    private List<WebElement> getTeamList(WebDriver driver) {
        List<WebElement> teamList = driver.findElements(By.className("row_live"));
        return teamList;
    }
    public void focusOn(String name,String pankou,String daxiaoqiu){
        //TODO 选中方法
        List<WebElement> teamList = this.getTeamList(driver);
        if(teamList.size()==0){
            JOptionPane.showMessageDialog(null, "队伍列表为空.", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        boolean flag=false;
        boolean pk_flag=false;
        boolean dxq_flag=false;

        for (int i = 0; i < teamList.size(); i++) {
            WebElement team = teamList.get(i);
            if (null != team) {
                List<WebElement> teamNameDiv = team.findElements(By.className("teamname_inner"));
                if (teamNameDiv.size() != 0) {
                    //主队名称
                    String zhudui = teamNameDiv.get(0).findElements(By.xpath("div")).get(1).getText();
                    if (name.equals(zhudui)) {
                        flag=true;
                        List<WebElement> pankouList = team.findElements(By.className("event_row"));
                        for (WebElement infoRow : pankouList) {
                            //获取主队大小球信息
                            List<WebElement> typeList = infoRow.findElements(By.className("event_even"));
                            if (typeList.size() > 2) {
                                List<WebElement> handiList = typeList.get(3).findElements(By.className("handi"));
                                if(null==handiList){
                                    continue;
                                }
                                WebElement pkElement = handiList.get(0);
                                if (null != pkElement) {
                                    WebElement pk = pkElement;
                                    String pkName = pkElement.getText();

                                    if (pkName.equals(pankou)) {
                                        pk_flag=true;
                                        List<WebElement> plElementList = typeList.get(4).findElements(By.className("odds"));
                                        setAttribuate(pk, "style", "background:#efce06;font-size:25px!important;padding:5px;color:red");

                                        action.click(pk).perform();
                                        if ("大".equals(daxiaoqiu)) {
                                            dxq_flag=true;
                                            WebElement xqPl = plElementList.get(0);
                                            action.click(xqPl).perform();
//                                            setAttribuate(xqPl, "style", "background:lightblue");

                                        } else if ("小".equals(daxiaoqiu)) {
                                            dxq_flag=true;
                                            WebElement dqPl = plElementList.get(1);
                                            action.click(dqPl).perform();
                                            dqPl.click();
//                                            setAttribuate(dqPl, "style", "background:lightblue");
                                        }


                                        break;

                                   }
                                }
                            }
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "队名没有获取到.", "提示", JOptionPane.INFORMATION_MESSAGE);

                }

            }
        }
        if(!flag){
            JOptionPane.showMessageDialog(null, "没有找到匹配的队伍.", "提示", JOptionPane.INFORMATION_MESSAGE);

        }
        if(!pk_flag){
            JOptionPane.showMessageDialog(null, "没有找到匹配的盘口.", "提示", JOptionPane.INFORMATION_MESSAGE);

        }
        if(!flag){
            JOptionPane.showMessageDialog(null, "没有找到匹配的队伍.", "提示", JOptionPane.INFORMATION_MESSAGE);

        }
        if(!dxq_flag){
            JOptionPane.showMessageDialog(null, "没有找到匹配的大小球.", "提示", JOptionPane.INFORMATION_MESSAGE);

        }

    }
}
