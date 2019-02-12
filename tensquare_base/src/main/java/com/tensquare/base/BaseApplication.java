package com.tensquare.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 基础微服务的启动引导类
 */
@SpringBootApplication
@EnableEurekaClient
public class BaseApplication {

    /**
     * 启动main函数
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class,args);
    }

    /**
     * 创建Id生成器
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
