package io.chan.productservice.aop.redis;

import io.chan.productservice.utils.CustomSpringELParser;
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

@Slf4j
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
  private static final String LOCK_PREFIX = "lock:";
  private final AopForTransaction aopForTransaction;
  private final RedissonClient redissonClient;

  @Pointcut("@annotation(io.chan.productservice.aop.redis.DistributedLock)")
  public void distributedLockAnnotation() {}

  @Around("distributedLockAnnotation()" + "&& @annotation(distributedLock)")
  public Object distributedLock(
      final ProceedingJoinPoint joinPoint, final DistributedLock distributedLock) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String key =
        LOCK_PREFIX
            + CustomSpringELParser.getDynamicValue(
                signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
    RLock rLock = redissonClient.getLock(key);

    try {
      boolean available =
          rLock.tryLock(
              distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
      if (!available) {
        throw new LockTimeoutException("Failed to acquire lock");
      }
      return aopForTransaction.proceed(joinPoint);
    } catch (InterruptedException e) {
      throw new InterruptedException();
    } finally {
      try {
        if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
          rLock.unlock();
        }
      } catch (IllegalMonitorStateException e) {
        log.info("Lock was not acquired by current thread");
      }
    }
  }
}
