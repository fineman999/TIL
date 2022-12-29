package practice.logtracer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import practice.logtracer.logtrace.FieldLogTrace;
import practice.logtracer.logtrace.LogTrace;
import practice.logtracer.logtrace.ThreadLocalLogTrace;

@Configuration
public class LogTraceConfig {
    @Bean
    public LogTrace logTrace(){
        return new ThreadLocalLogTrace();
    }

}
