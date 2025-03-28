package io.chan.queuingsystemforjava.domain.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.common.LoginMember;
import io.chan.queuingsystemforjava.domain.ticket.dto.event.SeatEvent;
import io.chan.queuingsystemforjava.domain.ticket.dto.response.SeatEventResponse;
import io.chan.queuingsystemforjava.global.security.MemberContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TicketSseController {

    private final ConcurrentMap<Long, ConcurrentMap<String, SseEmitter>> emitters =
            new ConcurrentHashMap<>();

    private final ExecutorService broadcastExecutor = Executors.newFixedThreadPool(10);
    private final ScheduledExecutorService keepAliveScheduler = Executors.newScheduledThreadPool(10); // 컨트롤러 수준에서 관리


    private final ObjectMapper objectMapper;

    @GetMapping(value = "/subscribe/performances/{performanceId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribePerformances(
            @LoginMember MemberContext memberContext, @PathVariable("performanceId") Long performanceId) {
        SseEmitter emitter = new SseEmitter(300_000L); // 5분
        String memberEmail = memberContext.getUsername();
        ConcurrentMap<String, SseEmitter> performanceEmitters =
                emitters.computeIfAbsent(performanceId, k -> new ConcurrentHashMap<>());
        performanceEmitters.put(memberEmail, emitter);

        // Keep-Alive 작업 스케줄링
        ScheduledFuture<?> keepAliveTask = keepAliveScheduler.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().comment("keep-alive"));
                log.debug("Keep-alive 전송 - 공연 ID: {}, 이메일: {}", performanceId, memberEmail);
            } catch (IOException e) {
                log.error("Keep-alive 전송 실패 - 공연 ID: {}, 이메일: {}", performanceId, memberEmail, e);
                removeEmitter(performanceId, memberEmail);
            }
        }, 0, 15, TimeUnit.SECONDS);

        emitter.onCompletion(() -> {
            keepAliveTask.cancel(false); // 작업 취소
            removeEmitter(performanceId, memberEmail);
        });
        emitter.onTimeout(() -> {
            keepAliveTask.cancel(false);
            sendErrorEvent(emitter, performanceId, memberEmail, "Connection timed out");
            removeEmitter(performanceId, memberEmail);
        });
        emitter.onError(e -> {
            keepAliveTask.cancel(false);
            sendErrorEvent(emitter, performanceId, memberEmail, "An error occurred: " + e.getMessage());
            removeEmitter(performanceId, memberEmail);
        });

        try {
            emitter.send(SseEmitter.event().name("INIT").data("공연 " + performanceId + "에 연결되었습니다"));
        } catch (IOException e) {
            keepAliveTask.cancel(false);
            sendErrorEvent(emitter, performanceId, memberEmail, "Failed to initialize connection");
            removeEmitter(performanceId, memberEmail);
        }

        return emitter;
    }

    private void removeEmitter(Long performanceId, String memberEmail) {
        ConcurrentMap<String, SseEmitter> performanceEmitters = emitters.get(performanceId);
        if (performanceEmitters != null) {
            performanceEmitters.remove(memberEmail);
            if (performanceEmitters.isEmpty()) {
                emitters.remove(performanceId);
                log.debug("공연 ID: {}에 대한 모든 이미터가 제거되었습니다", performanceId);
            }
        }
    }

    public void sendEventToPerformance(SeatEvent event) {
        Long performanceId = getPerformanceIdFromRequest().orElseThrow(() -> {
            log.error("performanceId를 찾을 수 없습니다.");
            return new IllegalArgumentException("performanceId를 찾을 수 없습니다.");
        });
        log.debug("공연 ID: {}, 이벤트: {}에 대한 브로드캐스트 시작", performanceId, event.getEventType());

        Optional.ofNullable(emitters.get(performanceId))
                .ifPresent(performanceEmitters -> {
                    String status = event.getEventType().equals(SeatEvent.EventType.SELECT) ? "SELECTED" : "SELECTABLE";
                    performanceEmitters.forEach((email, emitter) -> {
                        if (!email.equals(event.getMemberEmail())) {
                            broadcastExecutor.execute(() -> sendEventToEmitter(performanceId, event, email, emitter, status));
                        }
                    });
                });
        log.debug("공연 ID: {}, 이벤트: {}에 대한 브로드캐스트 완료", performanceId, event.getEventType());
    }

    private void sendEventToEmitter(Long performanceId, SeatEvent event, String email, SseEmitter emitter, String status) {
        try {
            SeatEventResponse eventData = new SeatEventResponse(event.getSeatId(), status);
            String jsonData = objectMapper.writeValueAsString(eventData);
            emitter.send(SseEmitter.event().name(event.getEventType().toString()).data(jsonData));
            log.debug("공연 ID: {}, 이메일: {}로 이벤트 전송 - 상태: {}", performanceId, email, status);
        } catch (IOException e) {
            log.info("클라이언트 연결 종료 감지 - 공연 ID: {}, 이메일: {}", performanceId, email);
            removeEmitter(performanceId, email); // 즉시 제거
        } catch (Exception e) {
            log.error("이벤트 전송 중 예상치 못한 오류 - 공연 ID: {}, 이메일: {}", performanceId, email, e);
            removeEmitter(performanceId, email);
        }
    }

    private void sendErrorEvent(SseEmitter emitter, Long performanceId, String memberEmail, String errorMessage) {
        try {
            emitter.send(SseEmitter.event().name("ERROR").data(errorMessage));
            emitter.complete();
        } catch (IOException e) {
            log.info("클라이언트 연결이 이미 종료됨 - 공연 ID: {}, 이메일: {}", performanceId, memberEmail);
            emitter.complete(); // 에러 전송 실패 시 바로 완료
        }
    }

    private Optional<Long> getPerformanceIdFromRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .map(request -> request.getHeader("performanceId"))
                .flatMap(this::parsePerformanceId);
    }

    private Optional<Long> parsePerformanceId(String performanceIdString) {
        try {
            return Optional.of(Long.parseLong(performanceIdString));
        } catch (NumberFormatException e) {
            log.error("performanceId 헤더 값을 Long으로 변환할 수 없습니다: {}", performanceIdString);
            return Optional.empty();
        }
    }

    @Scheduled(fixedRate = 60000)
    public void cleanUpEmitters() {
        emitters.forEach((performanceId, performanceEmitters) -> {
            performanceEmitters.entrySet().removeIf(entry -> {
                SseEmitter emitter = entry.getValue();
                try {
                    emitter.send(SseEmitter.event().comment("ping"));
                    return false;
                } catch (IOException e) {
                    log.debug("비활성 이미터 제거 - 공연 ID: {}, 이메일: {}", performanceId, entry.getKey());
                    emitter.complete();
                    return true;
                }
            });
            if (performanceEmitters.isEmpty()) {
                emitters.remove(performanceId);
            }
        });
    }
}