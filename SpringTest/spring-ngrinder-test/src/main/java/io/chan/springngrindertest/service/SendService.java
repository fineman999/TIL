package io.chan.springngrindertest.service;

import io.chan.springngrindertest.domain.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendService {

    private final NoticeService noticeService;
    // 원하는 스레드 풀 크기 설정
    int processors = Runtime.getRuntime().availableProcessors();
    int threadPoolSize = Math.max(2, processors); // 최소한 2개의 스레드는 사용
    ExecutorService customThreadPool = Executors.newWorkStealingPool(threadPoolSize);

    public long sendAllSync() {
        List<Notice> notices = noticeService.getAllNotices();
        long beforeTime = System.currentTimeMillis();

        /* 동기 방식 */
        notices.forEach(notice ->
                sendLog(notice.getTitle())
        );

        long afterTime = System.currentTimeMillis();
        long diffTime = afterTime - beforeTime;
        log.info("실행 시간(ms): " + diffTime);
        return diffTime;
    }

    public long sendAllAsync() {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();

        // common pool의 쓰레드 수 확인
        int commonPoolSize = commonPool.getParallelism();

        log.info("Common Pool Size: " + commonPoolSize + "/"+ processors);

        List<Notice> notices = noticeService.getAllNotices();
        long beforeTime = System.currentTimeMillis();

        /* 비동기 방식 */
        notices.forEach(notice ->
                CompletableFuture.runAsync(() -> sendLog(notice.getTitle()), customThreadPool)
                        .exceptionally(throwable -> {
                            log.error("Exception occurred: " + throwable.getMessage());
                            // 이슈 발생을 담당자가 인지 할수 있도록 추가적인 코드가 필요
                            return null;
                        })
        );

        long afterTime = System.currentTimeMillis();
        long diffTime = afterTime - beforeTime;
        log.info("실행 시간(ms): " + diffTime);
        return diffTime;
    }

    public void sendLog(String message) {
        try {
            Thread.sleep(1); // 임의의 작업시간을 주기위해 설정
            log.info("message : {}", message);
        }catch (Exception e) {
            log.error("[Error] : {} ",e.getMessage());
        }
    }
}
