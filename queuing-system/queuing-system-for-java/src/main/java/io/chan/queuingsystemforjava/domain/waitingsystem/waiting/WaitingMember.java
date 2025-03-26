package io.chan.queuingsystemforjava.domain.waitingsystem.waiting;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingMember {
    private String email;
    private long performanceId;
    private long waitingCount;
    private ZonedDateTime enteredAt;

    protected WaitingMember(String email, Long performanceId) {
        this(email, performanceId, 0, ZonedDateTime.now());
    }

    protected WaitingMember(
            String email, long performanceId, long waitingCount, ZonedDateTime enteredAt) {
        this.email = email;
        this.performanceId = performanceId;
        this.waitingCount = waitingCount;
        this.enteredAt = enteredAt;
    }

    public WaitingMember create(String email, long performanceId) {
        return new WaitingMember(email, performanceId);
    }

    public void updateWaitingInfo(long waitingCount, ZonedDateTime enteredAt) {
        this.waitingCount = waitingCount;
        this.enteredAt = enteredAt;
    }

    public void enter() {
        this.enteredAt = ZonedDateTime.now();
    }

}