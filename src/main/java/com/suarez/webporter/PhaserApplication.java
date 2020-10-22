package com.suarez.webporter;

import com.suarez.webporter.client.ConfigFrame;
import com.suarez.webporter.deal.BetWbDeal;
import com.suarez.webporter.driver.BetDriver;
import com.suarez.webporter.driver.NwbDriver;
import com.suarez.webporter.driver.WbDriver;
import com.suarez.webporter.util.SpringBeanUtil;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.suarez.webporter.deal","com.suarez.webporter.driver","com.suarez.webporter.util","com.suarez.webporter.client"})
public class PhaserApplication {
	private final static Logger logger = Logger.getLogger(PhaserApplication.class);


	public static void main(String[] args) {
		ApplicationContext acx = SpringApplication.run(PhaserApplication.class, args);
		SpringBeanUtil.setApplicationContext(acx);
		logger.info("程序开始启动...");

		// 可视化界面模型
		System.setProperty("java.awt.headless", "false");
		ConfigFrame cf=new ConfigFrame();
		cf.show();

		BetWbDeal deal = (BetWbDeal) acx.getBean("betWbDeal");
		Thread t = new Thread(deal);
		t.start();

//		WbDriver wbDriver = (WbDriver) acx.getBean("wbDriver");
//		wbDriver.init();
		NwbDriver nwbDriver = (NwbDriver) acx.getBean("nwbDriver");
		nwbDriver.init();

		BetDriver betDriver = (BetDriver) acx.getBean("betDriver");
		betDriver.init();
	}

}
