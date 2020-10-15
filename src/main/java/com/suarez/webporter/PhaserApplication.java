package com.suarez.webporter;

import com.suarez.webporter.client.ConfigFrame;
import com.suarez.webporter.core.app;
import com.suarez.webporter.util.SpringBeanUtil;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.suarez.webporter.core","com.suarez.webporter.util","com.suarez.webporter.client"})
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

//		app app = (com.suarez.webporter.core.app) acx.getBean("app");
//		Thread t = new Thread(app);
//		t.start();
	}

}
