package com.suarez.webporter.driver;

import com.suarez.webporter.deal.BetNwbDeal;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
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

    public void javaScriptClick(WebElement element) {
        try{
            if(element.isEnabled()&&element.isDisplayed()){
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();",element);
            }
            else{
                System.out.println("页面上的元素无法进行点击操作");
            }
        }catch(StaleElementReferenceException e){
            System.out.println("页面元素没有附加在网页中");
        }catch(NoSuchElementException e){
            System.out.println("在页面中没有找到要操作的元素");
        }catch(Exception e){
            System.out.println("无法完成单机动作"+e.getStackTrace());
        }
    }
    public void scrollbar(WebElement element) {
        try{
            if(element.isEnabled()&&element.isDisplayed()){
                ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", element);// 参数为true时调用该函数，页面（或容器）发生滚动，使element的顶部与视图（容器）顶部对齐；参数为false时，使element的底部与视图（容器）底部对齐。
            }
            else{
                System.out.println("页面上的元素无法进行点击操作");
            }
        }catch(StaleElementReferenceException e){
            System.out.println("页面元素没有附加在网页中");
        }catch(NoSuchElementException e){
            System.out.println("在页面中没有找到要操作的元素");
        }catch(Exception e){
            System.out.println("无法完成单机动作"+e.getStackTrace());
        }
    }
    private List<WebElement> getTeamList(WebDriver driver) {
        List<WebElement> teamList = driver.findElements(By.className("ovm-Fixture_Container"));
        return teamList;
    }
    public void focusOn(String name,String pankou,String daxiaoqiu){
        try {
            WebElement web = driver.findElement(By.xpath("//div[text()='" + name + "']"));
            scrollbar(web);
            try{
                if ("大".equals(daxiaoqiu)) {
                    WebElement daqiu = driver.findElements(By.xpath("//div[text()='"+name+"']//ancestor::div[@class='ovm-Fixture_Container']/div[2]//span[@class='ovm-ParticipantStackedCentered_Odds']")).get(0);
                    this.javaScriptClick(daqiu);

                } else if ("小".equals(daxiaoqiu)) {
                    WebElement xiaoqiu = driver.findElements(By.xpath("//div[text()='"+name+"']//ancestor::div[@class='ovm-Fixture_Container']/div[2]//span[@class='ovm-ParticipantStackedCentered_Odds']")).get(1);
                    this.javaScriptClick(xiaoqiu);
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "盘口已不存在.", "提示", JOptionPane.QUESTION_MESSAGE);
            }

        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "队伍已不存在.", "提示", JOptionPane.QUESTION_MESSAGE);
        }

//        //TODO 选中方法
//        List<WebElement> teamList = this.getTeamList(driver);
//        if(teamList.size()==0){
//            JOptionPane.showMessageDialog(null, "队伍列表为空.", "提示", JOptionPane.INFORMATION_MESSAGE);
//        }
//        boolean flag=false;
//        boolean pk_flag=false;
//        boolean dxq_flag=false;
//
//        for (int i = 0; i < teamList.size(); i++) {
//            WebElement team = teamList.get(i);
//            if (null != team) {
//                List<WebElement> teamNameList = team.findElements(By.className("ovm-FixtureDetailsTwoWay_TeamName"));
//                if (teamNameList.size() != 0) {
//                    //主队名称
//                    String zhudui = teamNameList.get(0).getText();
//                    if (name.equals(zhudui)) {
//                        flag=true;
//                        List<WebElement> pankouList = team.findElements(By.className("ovm-ParticipantStackedCentered_Handicap"));
//                        List<WebElement> plList = team.findElements(By.className("ovm-ParticipantStackedCentered_Odds"));
//
//                        //大球盘口
//                        WebElement dqPk = pankouList.get(0);
//                        WebElement xqPk = pankouList.get(1);
//
//                        String pkName = dqPk.getText();
//                        //小球盘口
//                        pkName = pkName.replace(",", "/");
//                        pkName = pkName.replace(".0", "");
//                        if (pkName.equals(pankou)) {
//                            pk_flag=true;
//                            if ("大".equals(daxiaoqiu)) {
//                                dxq_flag=true;
//                                WebElement dqPl = plList.get(0);
//                                this.javaScriptClick(dqPl);
//
//                            } else if ("小".equals(daxiaoqiu)) {
//                                dxq_flag=true;
//                                WebElement xqPl = plList.get(1);
//                                this.javaScriptClick(xqPl);
//                            }
//
//                            break;
//                        }
//
//                    }
//                }else{
//                    JOptionPane.showMessageDialog(null, "队名没有获取到.", "提示", JOptionPane.INFORMATION_MESSAGE);
//
//                }
//
//            }
//        }
//        if(!flag){
//            JOptionPane.showMessageDialog(null, "没有找到匹配的队伍.", "提示", JOptionPane.INFORMATION_MESSAGE);
//
//        }else{
//            if(!pk_flag){
//                JOptionPane.showMessageDialog(null, "没有找到匹配的盘口.", "提示", JOptionPane.INFORMATION_MESSAGE);
//
//            }else{
//                if(!dxq_flag){
//                    JOptionPane.showMessageDialog(null, "没有找到匹配的大小球.", "提示", JOptionPane.INFORMATION_MESSAGE);
//
//                }
//            }
//        }
    }
}
