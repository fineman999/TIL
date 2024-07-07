package io.chan.architecturepractice.notification.infrastructure;

import io.chan.architecturepractice.notification.application.channel.NotificationChannel;
import io.chan.architecturepractice.notification.domain.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificationChannel implements NotificationChannel {
    private final NotificationType type = NotificationType.EMAIL;

    @Override
    public void notify(final String message) {
        log.info("EmailNotificationChannel notify: {}", message);
    }

    @Override
    public boolean supports(final NotificationType type) {
        return this.type.equals(type);
    }
}
