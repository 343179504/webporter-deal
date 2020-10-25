package com.suarez.webporter.driver;

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

    private List<WebElement> getTeamList(WebDriver driver) {
        List<WebElement> teamList = driver.findElements(By.className("matchArea"));
        return teamList;
    }
    public void focusOn(String name,String pankou,String daxiaoqiu){
        try{
            WebElement web = driver.findElement(By.xpath("//span[text()='"+name+"']"));
            scrollbar(web);
            try{
                if ("大".equals(daxiaoqiu)) {
                    WebElement daqiu = driver.findElement(By.xpath("//span[text()='"+name+"']//ancestor::div[@class='multiOdds']/div[3]/div[@class='betArea'][1]//div"));

                    this.javaScriptClick(daqiu);

                } else if ("小".equals(daxiaoqiu)) {
                    WebElement xiaoqiu = driver.findElement(By.xpath("//span[text()='"+name+"']//ancestor::div[@class='multiOdds']/div[3]/div[@class='betArea'][2]//div"));

                    this.javaScriptClick(xiaoqiu);

                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "盘口已不存在.", "提示", JOptionPane.QUESTION_MESSAGE);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "队伍已不存在.", "提示", JOptionPane.QUESTION_MESSAGE);
        }


//        this.javaScriptClick(xiaoqiu);

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
//            try{
//                if (null != team) {
//                    //获取盘口列表
//                    List<WebElement> teamNameDivList = team.findElements(By.className("multiOdds"));
//                    WebElement firstPanKouElement = teamNameDivList.get(0);
//
//                    for(int j=0;j<teamNameDivList.size();j++){
//                        WebElement panKouElement = teamNameDivList.get(j);
//
//                        //获取第一个盘口，通过第一个盘口获取队名
//                            List<WebElement> spanElementList = firstPanKouElement.findElements(By.tagName("span"));
//                            if(spanElementList.size()==0){
//                                continue;
//                            }
//                            //主队名称
//                            String zhudui = spanElementList.get(0).getAttribute("innerHTML").trim();
//                            if (name.equals(zhudui)) {
//                                flag=true;
//                                List<WebElement> pankouList = panKouElement.findElements(By.className("odds"));
//                                if (pankouList.size() > 1) {
//                                    WebElement element = pankouList.get(1);
//
//                                    WebElement dqElement = element.findElements(By.className("betArea")).get(0);
//                                    WebElement xqElement = element.findElements(By.className("betArea")).get(1);
//                                    List<WebElement> dqSpanList = dqElement.findElements(By.tagName("span"));
//
//                                    String dqpkName = dqSpanList.get(0).getAttribute("innerHTML").trim();
//                                    pankou = pankou.replace(".0","");
//                                    if (dqpkName.equals(pankou)) {
//                                        pk_flag=true;
//                                        if ("大".equals(daxiaoqiu)) {
//                                            dxq_flag=true;
//                                            WebElement dqPl = dqElement.findElements(By.tagName("span")).get(1);
//                                            this.javaScriptClick(dqPl);
//
//                                        } else if ("小".equals(daxiaoqiu)) {
//                                            dxq_flag=true;
////                                    setAttribuate(xqElement, "style", "background:#efce06;font-size:25px!important;padding:5px;color:red");
//                                            WebElement xpPl = xqElement.findElements(By.tagName("span")).get(1);
//                                            this.javaScriptClick(xpPl);
//
//                                        }
//                                        break;
//                                    }
//                                }
//                        }
//                    }
//
//
//                }
//            }catch(Exception e){
//
//            }
//
//        }
//        if(!flag){
//            JOptionPane.showMessageDialog(null, "没有找到匹配的队伍.", "提示", JOptionPane.INFORMATION_MESSAGE);
//        }else{
//            if(!pk_flag){
//                JOptionPane.showMessageDialog(null, "没有找到匹配的盘口.", "提示", JOptionPane.INFORMATION_MESSAGE);
//
//            }else{
//
//                if(!dxq_flag){
//                    JOptionPane.showMessageDialog(null, "没有找到匹配的大小球.", "提示", JOptionPane.INFORMATION_MESSAGE);
//
//                }
//            }
//        }


    }
}
