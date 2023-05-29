package com.ihrm.employee;

import com.ihrm.common.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.ihrm",exclude ={ SecurityAutoConfiguration.class})
@EntityScan(basePackages = {"com.ihrm.domain.employee"})
//@EnableJpaRepositories(basePackages = {"com.ihrm.employee"})
//@ComponentScan(basePackages = {"com.ihrm.employee"})
@EnableEurekaClient
@EnableDiscoveryClient
public class EmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }

    @Bean
    public IdWorker idWorkker() {
        return new IdWorker(1, 1);
    }

  /*  @Bean
    public JwtUtils jwtUtil() {
        return new JwtUtils();
    }*/
}