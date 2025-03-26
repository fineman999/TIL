package io.chan.queuingsystemforjava.domain.waitingsystem.aop;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.waitingsystem.service.WaitingSystem;
import io.chan.queuingsystemforjava.global.security.AuthenticationToken;
import io.chan.queuingsystemforjava.global.security.MemberContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.Optional;

@Aspect
@RequiredArgsConstructor
public class WaitingAspect {

    private static final String PERFORMANCE_ID_HEADER = "performanceId";
    private static final String WAIT_ENDPOINT_FORMAT = "/api/v1/performances/%d/wait";

    private final WaitingSystem waitingSystem;

    // Waiting 어노테이션이 붙은 메서드 실행 시 동작
    @Around("@annotation(io.chan.queuingsystemforjava.domain.waitingsystem.aop.Waiting)")
    public Object handleWaitingRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getCurrentRequest(); // 현재 요청 가져오기
        long performanceId = extractPerformanceId(request); // 공연 ID 추출
        String email = extractUserEmail(); // 사용자 이메일 추출

        return processWaitingLogic(joinPoint, email, performanceId); // 대기 로직 처리
    }

    // 현재 HTTP 요청을 가져오는 메서드
    private HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
    }

    // 요청 헤더에서 공연 ID를 추출
    private long extractPerformanceId(HttpServletRequest request) {
        String performanceIdStr = request.getHeader(PERFORMANCE_ID_HEADER);
        try {
            return Long.parseLong(performanceIdStr);
        } catch (NumberFormatException | NullPointerException e) {
            throw new TicketingException(ErrorCode.NOT_CONTAINS_PERFORMANCE_INFO);
        }
    }

    // 인증 객체에서 사용자 이메일을 안전하게 추출
    private String extractUserEmail() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(AuthenticationToken.class::isInstance)
                .map(AuthenticationToken.class::cast)
                .map(AuthenticationToken::getPrincipal)
                .filter(MemberContext.class::isInstance)
                .map(MemberContext.class::cast)
                .map(MemberContext::getUsername)
                .orElseThrow(() -> new TicketingException(ErrorCode.INVALID_AUTHENTICATION));
    }

    // 대기 시스템 로직 처리
    private Object processWaitingLogic(ProceedingJoinPoint joinPoint, String email, long performanceId)
            throws Throwable {
        if (waitingSystem.isReadyToHandle(email, performanceId)) {
            return joinPoint.proceed(); // 준비되었다면 원래 요청 실행
        }

        waitingSystem.enterWaitingRoom(email, performanceId); // 대기실 입장
        String redirectUrl = String.format(WAIT_ENDPOINT_FORMAT, performanceId);
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(redirectUrl))
                .build(); // 대기 페이지로 리다이렉트
    }
}