package com.campus.canteen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.campus.canteen.config.AIConfig;

@SpringBootApplication
@MapperScan("com.campus.canteen.mapper")
@EnableConfigurationProperties(AIConfig.class)
public class CanteenApplication {
    public static void main(String[] args) {
        SpringApplication.run(CanteenApplication.class, args);
    }
}