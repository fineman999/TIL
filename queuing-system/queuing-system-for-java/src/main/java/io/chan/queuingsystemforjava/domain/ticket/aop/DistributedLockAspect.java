package io.chan.queuingsystemforjava.domain.ticket.aop;

import io.chan.queuingsystemforjava.global.utils.CustomSpringELParser;
import jakarta.persistence.LockTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
  private static final String LOCK_PREFIX = "lock::"; // 상수 클래스 분리 고려

  private final AopForTransaction aopForTransaction;
  private final RedissonClient redissonClient;

  @Pointcut("@annotation(io.chan.queuingsystemforjava.domain.ticket.aop.DistributedLock)")
  public void distributedLockAnnotation() {}

  @Around("distributedLockAnnotation() && @annotation(distributedLock)")
  public Object distributedLock(final ProceedingJoinPoint joinPoint, final DistributedLock distributedLock) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String key = LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
            signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
    RLock rLock = redissonClient.getLock(key);

    try {
      if (!acquireLock(rLock, distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit())) {
        throw new LockTimeoutException("Failed to acquire lock for key: " + key);
      }
      return aopForTransaction.proceed(joinPoint);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt(); // 인터럽트 상태 복원
      throw new InterruptedException("Lock acquisition interrupted for key: " + key);
    } finally {
      releaseLock(rLock, key);
    }
  }

  private boolean acquireLock(RLock rLock, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
    return rLock.tryLock(waitTime, leaseTime, timeUnit);
  }

  private void releaseLock(RLock rLock, String key) {
    try {
      if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
        rLock.unlock();
      }
    } catch (IllegalMonitorStateException e) {
      log.info("Lock was not held by current thread for key: {}", key);
    }
  }
}