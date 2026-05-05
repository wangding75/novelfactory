package com.novelfactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NovelFactoryApplication {
  public static final String APP_NAME = "novel-factory";

  public static void main(String[] args) {
    SpringApplication.run(NovelFactoryApplication.class, args);
  }
}
