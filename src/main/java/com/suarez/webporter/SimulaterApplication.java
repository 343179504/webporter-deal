package com.suarez.webporter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// 扫描 mybatis 通用 mapper 所在的包
@MapperScan(basePackages = "com.adao.simulater.mapper")
// 扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.adao.simulater"})
@EnableConfigurationProperties(Constant.class)
public class SimulaterApplication {
    private final static Logger logger = Logger.getLogger(SimulaterApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SimulaterApplication.class, args);

        logger.info("程序开始启动...");

        // 可视化界面模型
        ConfigFrame cf=new ConfigFrame();
        cf.show();
    }

}
