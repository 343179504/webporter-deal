package com.suarez.webporter.driver;

import com.suarez.webporter.deal.BetNwbDeal;
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
public class BetDriver {
    @Autowired
    private WebporterConfig webporterConfig;

    private WebDriver driver;
    private Actions action;
    public void init(){
        System.setProperty("webdriver.chrome.driver", webporterConfig.getWebdriverAddresses());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.setExperimentalOption("debuggerAddress", webporterConfig.getBet_listenAddress());
        driver = new ChromeDriver(options);
        action = new Actions(driver);
//        driver.switchTo().frame("sb_frame");


    }
    public void setAttribuate(WebElement eleemnt, String attrName, String attrValue){


        ((JavascriptExecutor)driver).executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", eleemnt,attrName,attrValue);

    }

    public void removeAttribuate(WebElement eleemnt,String attrName,String attrValue)
    {
        ((JavascriptExecutor)driver).executeScript("arguments[0].removeAttribute(arguments[1],arguments[2])", eleemnt,attrName,attrValue);

    }
    private List<WebElement> getTeamList(WebDriver driver) {
        List<WebElement> teamList = driver.findElements(By.className("ovm-Fixture_Container"));
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
                List<WebElement> teamNameList = team.findElements(By.className("ovm-FixtureDetailsTwoWay_TeamName"));
                if (teamNameList.size() != 0) {
                    //主队名称
                    String zhudui = teamNameList.get(0).getText();
                    if (name.equals(zhudui)) {
                        flag=true;
                        List<WebElement> pankouList = team.findElements(By.className("ovm-ParticipantStackedCentered_Handicap"));
                        List<WebElement> plList = team.findElements(By.className("ovm-ParticipantStackedCentered_Odds"));

                        //大球盘口
                        WebElement dqPk = pankouList.get(0);
                        WebElement xqPk = pankouList.get(1);

                        String pkName = dqPk.getText();
                        //小球盘口
                        pkName = pkName.replace(",", "/");
                        pkName = pkName.replace(".0", "");
//                        pkName=BetNwbDeal.dealPoint(pkName);
                        if (pkName.equals(pankou)) {
                            pk_flag=true;
                            if ("大".equals(daxiaoqiu)) {
                                dxq_flag=true;
//                              setAttribuate(dqPk, "style", "background:#efce06;font-size:25px!important;padding:5px;color:red");
                                WebElement dqPl = plList.get(0);
                                try{
                                    dqPl.click();
                                }catch (Exception e){
                                    action.click(dqPl).perform();
                                }

                            } else if ("小".equals(daxiaoqiu)) {
                                dxq_flag=true;
                                WebElement xqPl = plList.get(1);
                                try{
                                    xqPl.click();
                                }catch (Exception e){
                                    action.click(xqPl).perform();
//                                    setAttribuate(xqPk, "style", "background:#efce06;font-size:25px!important;padding:5px;color:red");

                                }
                            }

                            break;
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
