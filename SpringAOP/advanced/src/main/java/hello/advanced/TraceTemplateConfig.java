package hello.advanced;

import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceTemplateConfig {

    private final LogTrace logTrace;

    public TraceTemplateConfig(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Bean
    public TraceTemplate template() {
        return new TraceTemplate(logTrace);
    }

}
