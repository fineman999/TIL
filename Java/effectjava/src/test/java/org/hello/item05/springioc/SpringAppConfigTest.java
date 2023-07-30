package org.hello.item05.springioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class SpringAppConfigTest {

    @Test
    @DisplayName("SpellChecker 빈이 생성되는지 확인")
    void create() {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        SpellChecker spellChecker = applicationContext.getBean(SpellChecker.class);
        assertThat(spellChecker.isValid("Spring")).isTrue();
    }
}