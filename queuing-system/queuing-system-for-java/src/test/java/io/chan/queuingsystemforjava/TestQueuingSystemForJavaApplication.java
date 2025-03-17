package io.chan.queuingsystemforjava;

import org.springframework.boot.SpringApplication;

public class TestQueuingSystemForJavaApplication {

  public static void main(String[] args) {
    SpringApplication.from(QueuingSystemForJavaApplication::main)
        .with(TestcontainersConfig.class)
        .run(args);
  }
}
