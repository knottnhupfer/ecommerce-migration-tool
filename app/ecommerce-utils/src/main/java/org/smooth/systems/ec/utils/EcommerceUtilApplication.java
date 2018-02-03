package org.smooth.systems.ec.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "org.smooth.systems.ec.utils" })
public class EcommerceUtilApplication {
  public static void main(String[] args) {
    SpringApplication.run(EcommerceUtilApplication.class, args);
  }
}