package com.novelfactory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class NovelFactoryApplicationTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  void contextLoads_andRegistersApplicationBean() {
    assertThat(applicationContext).isNotNull();
    assertThat(applicationContext.getBean(NovelFactoryApplication.class)).isNotNull();
  }
}
