package io.chan.architecturepractice.notification.application;

import io.chan.architecturepractice.notification.application.channel.NotificationChannel;
import io.chan.architecturepractice.notification.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final List<NotificationChannel> notificationChannels;

    public void notify(final NotificationType type, final String message) {
        notificationChannels.stream()
                .filter(channel -> channel.supports(type))
                .forEach(channel -> channel.notify(message));
    }
}
