package com.lxc;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages = "com.lxc")
@MapperScan("com.lxc.dao")
@EnableScheduling   //开启定时任务
@EnableTransactionManagement  //开启事务支持
public class MistoreServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MistoreServerApplication.class, args);
    }

}
