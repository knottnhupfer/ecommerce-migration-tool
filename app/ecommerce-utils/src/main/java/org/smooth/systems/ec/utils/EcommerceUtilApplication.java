package org.smooth.systems.ec.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = { "org.smooth.systems.ec" })
@EnableJpaRepositories(basePackages = { "org.smooth.systems.ec" })
@ComponentScan(basePackages = { "org.smooth.systems.ec" })
public class EcommerceUtilApplication {
  public static void main(String[] args) {
    SpringApplication.run(EcommerceUtilApplication.class, args);
  }
}