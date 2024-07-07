package io.chan.architecturepractice.notification.application;

import io.chan.architecturepractice.notification.domain.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationServiceTest {

  @Autowired private NotificationService notificationService;

  @DisplayName("여러 개의 채널을 List로 의존성을 주입받아 notify 메서드를 실행하면 해당 채널이 지원하는 경우 메시지를 전송한다.")
  @Test
  void notifyEmail() {

    // when
    notificationService.notify(NotificationType.EMAIL, "test message");
  }

    @DisplayName("여러 개의 채널을 List로 의존성을 주입받아 notify 메서드를 실행하면 해당 채널이 지원하지 않는 경우 메시지를 전송하지 않는다.")
    @Test
    void notifyPush() {

        // when
        notificationService.notify(NotificationType.PUSH, "test message");
    }
}
