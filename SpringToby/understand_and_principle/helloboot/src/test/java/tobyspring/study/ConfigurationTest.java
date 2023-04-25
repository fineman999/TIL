package tobyspring.study;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.*;

public class ConfigurationTest {

    @Test
    void basicConfiguration() {
        MyConfig myConfig = new MyConfig();
        Bean1 bean1 = myConfig.bean1();
        Bean2 bean2 = myConfig.bean2();
        assertThat(bean1.common).isNotSameAs(bean2.common);
    }

    @Test
    void configuration() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.register(MyConfig.class);
        ac.refresh();
        Bean1 bean1 = ac.getBean(Bean1.class);
        Bean2 bean2 = ac.getBean(Bean2.class);
        assertThat(bean1.common).isSameAs(bean2.common);

    }

    @Test
    void proxyCommonMethod() {
        MyConfigProxy myConfigProxy = new MyConfigProxy();

        Bean1 bean1 = myConfigProxy.bean1();
        Bean2 bean2 = myConfigProxy.bean2();
        assertThat(bean1.common).isSameAs(bean2.common);
    }

    static class MyConfigProxy extends MyConfig {
        private Common common;
        @Override
        Common common() {
            if (this.common == null) this.common = super.common();

            return this.common;
        }
    }
    @Configuration
    static class MyConfig {
        @Bean
        Common common() {
            return new Common();
        }

        @Bean
        Bean1 bean1() {
            return new Bean1(common());
        }

        @Bean
        Bean2 bean2() {
            return new Bean2(common());
        }

    }
    // bean1 <-- Common에 의존
    // bean2 <-- Common에 의존
    static class Bean1 {
        private final Common common;

        Bean1(Common common) {
            this.common = common;
        }
    }
    static class Bean2 {
        private final Common common;

        Bean2(Common common) {
            this.common = common;
        }
    }


    private static class Common {
    }

}
