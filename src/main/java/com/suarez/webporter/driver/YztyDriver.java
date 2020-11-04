package com.suarez.webporter.driver;

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
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.suarez.webporter.deal.BetNwbDeal.getDoubleString;
import static java.lang.Thread.sleep;

@Slf4j
@Component
public class YztyDriver {
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
        options.setExperimentalOption("debuggerAddress", webporterConfig.getYzty_listenAddress());
        driver = new ChromeDriver(options);
        action = new Actions(driver);
        //设置隐式等待
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        WebElement frame=driver.findElement(By.xpath( "/html//iframe" ));
        driver.switchTo().frame(frame);
        List<WebElement> second=driver.findElements(By.id("sportsFrame"));
        driver.switchTo().frame(second.get(0));

    }
    public void setAttribuate(WebElement eleemnt, String attrName, String attrValue){


        ((JavascriptExecutor)driver).executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", eleemnt,attrName,attrValue);

    }

    public void removeAttribuate(WebElement eleemnt,String attrName,String attrValue)
    {
        ((JavascriptExecutor)driver).executeScript("arguments[0].removeAttribute(arguments[1],arguments[2])", eleemnt,attrName,attrValue);

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

    public void setMoneyValue(WebElement input,String je) {
        if(je.indexOf(".")>0){
            je=je.substring(0,je.indexOf("."));
        }
        input.clear();
        input.sendKeys(je);
    }

    public static String dealPoint(String point) {
        String realPoint = point;
        if (point.contains(".25") || point.contains(".75")) {
            double pointDouble = Double.parseDouble(point);
            String sm_point = getDoubleString(pointDouble - 0.25);
            String big_point = getDoubleString(pointDouble + 0.25);
            realPoint = sm_point + "/" + big_point;
        }else{
            double pointDouble = Double.parseDouble(point);
            realPoint= String.valueOf(pointDouble);
        }
        return realPoint;
    }

    public boolean enterMoney(String pankou,String daxiaoqiu,String je) {
        try{
            //todo 判断投注框是否打开以及可输入
            //获取投注框
            List<WebElement> tzk = driver.findElements(By.xpath("//div[@class='ticket']"));


            if(tzk.size()>0){
                //todo 判断是否可输入
                //获取投注单
                List<WebElement> sfky = tzk.get(0).findElements(By.xpath("div[@class='betInfoArea']//div[@class='betDetial']"));
                    boolean flag=false;
                    if(sfky.size()>0){
                        WebElement dxqEle = sfky.get(0).findElement(By.xpath("div[@class='name ']"));
                        WebElement pkEle = sfky.get(0).findElement(By.xpath("div[@class='oddsDetail']//span[@class='selectorName']"));
                        if("大".equals(daxiaoqiu)){
                            String dqdxq=dxqEle.getText();
                            if(dqdxq.equals("大")){
                                String pk=dealPoint(pkEle.getText());
                                pk=pk.replace(".0","");
                                if(pk.equals(pankou)){
                                    flag=true;
                                }
                            }
                        }else{
                            String dqdxq=dxqEle.getText();
                            if(dqdxq.equals("小")){
                                String pk=dealPoint(pkEle.getText());
                                pk=pk.replace(".0","");
                                if(pk.equals(pankou)){
                                    flag=true;
                                }
                            }
                        }
                    }
                    //判断是否可输入
                    if(flag){
                        //todo 输入金额
                        WebElement input =tzk.get(0).findElement(By.xpath("div[@class='betOtherArea']//input[@id='betSlipStake']"));

                        WebElement zgtzEle = driver.findElement(By.xpath("//div[@id='mainSection']//span[text()='最高投注']/following-sibling::span[1]"));
                        String vzgtz = zgtzEle.getText().trim();
                        boolean kfFlag = false;
                        if(vzgtz.length()>0){
                            kfFlag=true;
                        }else{
                            while(true){
                                vzgtz = zgtzEle.getText();
                                if(vzgtz.length()>0){
                                    kfFlag=true;
                                    break;
                                }
                            }
                        }
                        if(kfFlag){
                            this.setMoneyValue(input,je);
                        }

//                        input.click();

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
        try{
            if("小".equals(daxiaoqiu)){
                je = String.valueOf(dealConfig.getWebpoterPhaseSmMoney());
            }
            WebElement web = null;
            try{
                web = driver.findElement(By.xpath("//span[text()='"+name+"']"));

            }catch (Exception e){
                web = driver.findElement(By.xpath("//span[text()='"+name+" ']"));
            }
            scrollbar(web);
            try{
                if ("大".equals(daxiaoqiu)) {
                    WebElement daqiu = driver.findElement(By.xpath("//span[text()='"+name+"']//ancestor::div[@class='multiOdds']/div[3]/div[@class='betArea'][1]//div"));

                    boolean clickFlag =this.javaScriptClick(daqiu);
                    if(clickFlag){
                        this.enterMoney(pankou,daxiaoqiu,je);
                    }

                } else if ("小".equals(daxiaoqiu)) {
                    WebElement xiaoqiu = driver.findElement(By.xpath("//span[text()='"+name+"']//ancestor::div[@class='multiOdds']/div[3]/div[@class='betArea'][2]//div"));

                    boolean clickFlag =this.javaScriptClick(xiaoqiu);
                    if(clickFlag){
                        this.enterMoney(pankou,daxiaoqiu,je);
                    }

                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "盘口已不存在.", "提示", JOptionPane.QUESTION_MESSAGE);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "队伍已不存在.", "提示", JOptionPane.QUESTION_MESSAGE);
        }


    }
}
