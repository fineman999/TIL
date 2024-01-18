package io.chan.springslacktest;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SlackService {

    private final SlackConfig slackConfig;

    @PostMapping("/slack")
    public ResponseEntity<ChatPostMessageResponse> slack() throws SlackApiException, IOException {
        String channelId = slackConfig.getChannel();
        String text = ":wave: Hi from a bot written in Java!";

        Slack slack = Slack.getInstance();
        ChatPostMessageResponse response = slack.methods(slackConfig.getToken()).chatPostMessage(req -> req
                .channel(channelId)
                .text(text)
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/slack-async")
    public ResponseEntity<Void> slackAsync() {
        String channelId = slackConfig.getChannel();
        String text = ":wave: Hi from a bot written in Java!";

        Slack slack = Slack.getInstance();

        slack.methodsAsync(slackConfig.getToken()).chatPostMessage(req -> req
                .channel(channelId)
                .text(text)
        );

        return ResponseEntity.ok().build();
    }



}
