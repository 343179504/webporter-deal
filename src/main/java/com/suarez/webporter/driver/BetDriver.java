package com.suarez.webporter.driver;

import com.suarez.webporter.deal.BetNwbDeal;
import com.suarez.webporter.deal.DealConfig;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@Slf4j
@Component
public class BetDriver {
    @Autowired
    private WebporterConfig webporterConfig;
    @Autowired
    protected DealConfig dealConfig;

    private WebDriver driver;
    private Actions action;
    public void init(){
        System.setProperty("webdriver.chrome.driver", webporterConfig.getWebdriverAddresses());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.setExperimentalOption("debuggerAddress", webporterConfig.getBet_listenAddress());
        driver = new ChromeDriver(options);
        action = new Actions(driver);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

//        driver.switchTo().frame("sb_frame");


    }
    public void setAttribuate(WebElement eleemnt, String attrName, String attrValue){


        ((JavascriptExecutor)driver).executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", eleemnt,attrName,attrValue);

    }

    public void removeAttribuate(WebElement eleemnt,String attrName,String attrValue)
    {
        ((JavascriptExecutor)driver).executeScript("arguments[0].removeAttribute(arguments[1],arguments[2])", eleemnt,attrName,attrValue);

    }

    public boolean javaScriptClick(WebElement element) {
        try{
            if(element.isEnabled()&&element.isDisplayed()){
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();",element);
                return true;
            }
            else{
                return false;
            }
        }catch(StaleElementReferenceException e){
            System.out.println("页面元素没有附加在网页中");
        }catch(NoSuchElementException e){
            System.out.println("在页面中没有找到要操作的元素");
        }catch(Exception e){
            System.out.println("无法完成单机动作"+e.getStackTrace());
        }
        return false;
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
    public void setMoneyValue(String je) {
        if(je.indexOf(".")>0){
            je=je.substring(0,je.indexOf("."));
        }
        String[] jeArray = je.split("");
        for(int m=0;m<jeArray.length;m++){
            String num = jeArray[m];
            switch(num)
            {
                case "0" :
                    action.sendKeys(Keys.NUMPAD0).perform();
                    break;
                case "1" :
                    action.sendKeys(Keys.NUMPAD1).perform();
                    break;
                case "2" :
                    action.sendKeys(Keys.NUMPAD2).perform();
                    break;
                case "3" :
                    action.sendKeys(Keys.NUMPAD3).perform();
                    break;
                case "4" :
                    action.sendKeys(Keys.NUMPAD4).perform();
                    break;
                case "5" :
                    action.sendKeys(Keys.NUMPAD5).perform();
                    break;
                case "6" :
                    action.sendKeys(Keys.NUMPAD6).perform();
                    break;
                case "7" :
                    action.sendKeys(Keys.NUMPAD7).perform();
                    break;
                case "8" :
                    action.sendKeys(Keys.NUMPAD8).perform();
                    break;
                case "9" :
                    action.sendKeys(Keys.NUMPAD9).perform();
                    break;
                default :
                    break;
            }
        }

    }

    public boolean enterMoney(String pankou,String daxiaoqiu,String je) {
        try{
            //todo 判断投注框是否打开以及可输入
//            WebDriverWait wait = new WebDriverWait(driver, 3);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='bss-StandardBetslip bss-StandardBetslip_HasInPlayBet ']")));
            //获取投注框
            List<WebElement> tzk = driver.findElements(By.xpath("//div[@class='bss-StandardBetslip bss-StandardBetslip_HasInPlayBet ']"));

            if(tzk.size()>0){
                //todo 判断是否可输入
                //获取比赛投注栏
                List<WebElement> sfky = tzk.get(0).findElements(By.xpath("div[@class='bss-StandardBetslip_ContentWrapper ']//div[@class='bss-NormalBetItem ']"));
                for(int i=0;i<sfky.size();i++){
                    boolean flag=false;
                    if(sfky.size()>0){
                        WebElement dxqEle = sfky.get(i).findElement(By.xpath("div[@class='bss-NormalBetItem_Wrapper ']//div[@class='bss-NormalBetItem_Title ']"));
                        WebElement plEle = sfky.get(i).findElement(By.xpath("div[@class='bss-NormalBetItem_Wrapper ']//div[@class='bss-NormalBetItem_Handicap ']"));
                        if("大".equals(daxiaoqiu)){
                            String dqdxq=dxqEle.getText();
                            if(dqdxq.contains("高于")){
                                String dqpl=plEle.getText().replace(",","/");
                                dqpl=dqpl.replace(".0","");

                                if(dqpl.equals(pankou)){
                                    flag=true;
                                }
                            }
                        }else{
                            String dqdxq=dxqEle.getText();
                            if(dqdxq.contains("低于")){
                                String dqpl=plEle.getText().replace(",","/");
                                dqpl=dqpl.replace(".0","");

                                if(dqpl.equals(pankou)){
                                    flag=true;
                                }
                            }
                        }
                    }
                    //判断是否可输入
                    //todo 获取投注单中的 盘口、大小球并校验是否正确
                    if(flag){
                        //todo 输入金额
                        WebElement element =sfky.get(i).findElement(By.xpath("div[@class='bss-NormalBetItem_Wrapper ']//div[@class='bss-StakeBox_StakeAndReturn ']"));
                        WebElement input = element.findElement(By.xpath("div[@class='bss-StakeBox_StakeInput ']"));

                        WebElement value = input.findElements(By.tagName("div")).get(1);
                        value.click();
                        sleep(5);
                        this.setMoneyValue(je);
                        break;

                    }
                }
            }
        }catch(StaleElementReferenceException e){
            System.out.println("页面元素没有附加在网页中");
        }catch(NoSuchElementException e){
            System.out.println("在页面中没有找到要操作的元素");
        }catch(Exception e){
            System.out.println("无法完成单机动作"+e.getStackTrace());
        }
        return false;
    }

    public void focusOn(String name,String pankou,String daxiaoqiu,String je){
        try {
            if("小".equals(daxiaoqiu)){
                je = String.valueOf(dealConfig.getWebpoterPhaseSmMoney());
            }
            WebElement web = driver.findElement(By.xpath("//div[text()='" + name + "']"));
            scrollbar(web);
            try{
                if ("大".equals(daxiaoqiu)) {
                    WebElement daqiu = driver.findElements(By.xpath("//div[text()='"+name+"']//ancestor::div[@class='ovm-Fixture_Container']/div[2]//span[@class='ovm-ParticipantStackedCentered_Odds']")).get(0);
                    boolean clickFlag =this.javaScriptClick(daqiu);
                    if(clickFlag){
                        this.enterMoney(pankou,daxiaoqiu,je);
                    }

                } else if ("小".equals(daxiaoqiu)) {
                    WebElement xiaoqiu = driver.findElements(By.xpath("//div[text()='"+name+"']//ancestor::div[@class='ovm-Fixture_Container']/div[2]//span[@class='ovm-ParticipantStackedCentered_Odds']")).get(1);
                    boolean clickFlag =this.javaScriptClick(xiaoqiu);
                    if(clickFlag){
                        this.enterMoney(pankou,daxiaoqiu,je);
                    }

                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "盘口已不存在.", "提示", JOptionPane.QUESTION_MESSAGE);
            }

        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "队伍已不存在.", "提示", JOptionPane.QUESTION_MESSAGE);
        }

    }
}
