//package io.security.oauth2.common.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//
//@Slf4j
//public class CustomSecurityConfigurer extends AbstractHttpConfigurer<CustomSecurityConfigurer, HttpSecurity> {
//
//    private boolean isSecure;
//
//    @Override
//    public void init(HttpSecurity http) throws Exception {
//
//        super.init(http);
//        log.info("CustomSecurityConfigurer init");
//    }
//
//    @Override
//    public void configure(HttpSecurity builder) throws Exception {
//        super.configure(builder);
//        log.info("CustomSecurityConfigurer configure");
//        if (isSecure) {
//         log.info("https is required");
//        }else {
//            log.info("http is secured");
//        }
//    }
//
//    public CustomSecurityConfigurer setFlag(boolean secure) {
//        this.isSecure = secure;
//        return this;
//    }
//}
