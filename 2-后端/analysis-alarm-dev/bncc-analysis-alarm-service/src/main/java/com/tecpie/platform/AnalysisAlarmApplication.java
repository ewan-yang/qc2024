package com.tecpie.platform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author tecpie
 */
@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.tecpie"})
@EnableFeignClients(basePackages = {"com.tecpie.platform.common.feign"})
public class AnalysisAlarmApplication {

  public static void main(String[] args) {
    SpringApplication.run(AnalysisAlarmApplication.class, args);
    log.info("Swagger：http://localhost:18082/v3/api-docs");
  }
}
