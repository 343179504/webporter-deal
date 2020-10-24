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
public class YztyDriver {
    @Autowired
    private WebporterConfig webporterConfig;

    private WebDriver driver;
    private Actions action;
    public void init(){
        System.setProperty("webdriver.chrome.driver", webporterConfig.getWebdriverAddresses());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.setExperimentalOption("debuggerAddress", webporterConfig.getYzty_listenAddress());
        driver = new ChromeDriver(options);
        action = new Actions(driver);
        driver.switchTo().frame("sb_frame");
        driver.switchTo().frame("sportsFrame");

    }
    public void setAttribuate(WebElement eleemnt, String attrName, String attrValue){


        ((JavascriptExecutor)driver).executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", eleemnt,attrName,attrValue);

    }

    public void removeAttribuate(WebElement eleemnt,String attrName,String attrValue)
    {
        ((JavascriptExecutor)driver).executeScript("arguments[0].removeAttribute(arguments[1],arguments[2])", eleemnt,attrName,attrValue);

    }
    private List<WebElement> getTeamList(WebDriver driver) {
        List<WebElement> teamList = driver.findElements(By.className("matchArea"));
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
            try{
                if (null != team) {
                    //获取盘口列表
                    List<WebElement> teamNameDiv = team.findElements(By.className("multiOdds"));
                    if (teamNameDiv.size() != 0) {
                        //获取第一个盘口，通过第一个盘口获取队名
                        WebElement firstPanKouElement = teamNameDiv.get(0);
                        List<WebElement> spanElementList = firstPanKouElement.findElements(By.tagName("span"));
                        if(spanElementList.size()==0){
                            continue;
                        }
                        //主队名称
                        String zhudui = spanElementList.get(0).getAttribute("innerHTML").trim();
                        if (name.equals(zhudui)) {
                            flag=true;
                            List<WebElement> pankouList = firstPanKouElement.findElements(By.className("odds"));
                            if (pankouList.size() > 1) {
                                WebElement element = pankouList.get(1);

                                WebElement dqElement = element.findElements(By.className("betArea")).get(0);
                                WebElement xqElement = element.findElements(By.className("betArea")).get(1);
                                List<WebElement> dqSpanList = dqElement.findElements(By.tagName("span"));

                                String dqpkName = dqSpanList.get(0).getAttribute("innerHTML").trim();
                                pankou = pankou.replace(".0","");
                                if (dqpkName.equals(pankou)) {
                                    pk_flag=true;
                                    if ("大".equals(daxiaoqiu)) {
                                        dxq_flag=true;
//                                    setAttribuate(dqElement, "style", "background:#efce06;font-size:25px!important;padding:5px;color:red");
                                        WebElement dqPl = dqElement.findElements(By.tagName("span")).get(1);
//                                    action.click(dqPl).perform();
//                                    dqPl.click();
                                        try{
                                            dqPl.click();
                                        }catch (Exception e){
                                            action.click(dqPl).perform();
                                        }

                                    } else if ("小".equals(daxiaoqiu)) {
                                        dxq_flag=true;
//                                    setAttribuate(xqElement, "style", "background:#efce06;font-size:25px!important;padding:5px;color:red");
                                        WebElement xpPl = xqElement.findElements(By.tagName("span")).get(1);
                                        try{
                                            xpPl.click();
                                        }catch (Exception e){
                                            action.click(xpPl).perform();

                                        }
                                    }
                                    break;
                                }
                            }


                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "队名没有获取到.", "提示", JOptionPane.INFORMATION_MESSAGE);

                    }

                }
            }catch(Exception e){

            }

        }
        if(!flag){
            JOptionPane.showMessageDialog(null, "没有找到匹配的队伍.", "提示", JOptionPane.INFORMATION_MESSAGE);

        }
        if(!pk_flag){
            JOptionPane.showMessageDialog(null, "没有找到匹配的盘口.", "提示", JOptionPane.INFORMATION_MESSAGE);

        }
        if(!dxq_flag){
            JOptionPane.showMessageDialog(null, "没有找到匹配的大小球.", "提示", JOptionPane.INFORMATION_MESSAGE);

        }

    }
}
