package org.smooth.systems.ec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "org.smooth.systems.ec" })
@SpringBootApplication
public class EcommerceMigrationApplication {

  public static void main(String[] args) {
    SpringApplication.run(EcommerceMigrationApplication.class, args);
  }
}
