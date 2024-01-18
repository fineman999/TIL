package io.chan.springslacktest;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Getter
@Component
public class SlackConfig {
    private final String token;
    private final String channel;


    public SlackConfig(
            @Value("${slack.token}")
            String token,
            @Value("${slack.channel.monitor}")
            String channel) {
        this.token = token;
        this.channel = channel;
    }
}
