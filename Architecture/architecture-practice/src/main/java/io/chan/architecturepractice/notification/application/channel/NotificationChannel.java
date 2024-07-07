package io.chan.architecturepractice.notification.application.channel;

import io.chan.architecturepractice.notification.domain.NotificationType;

public interface NotificationChannel {
    void notify(String message);

    boolean supports(NotificationType type);
}
