package practice.logtracer.logtrace.threadlocal;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import practice.logtracer.logtrace.threadlocal.code.FieldService;

@Slf4j
public class FieldServiceTest {
    private FieldService fieldService = new FieldService();

    @Test
    void field(){
        log.info("main start");
//        Runnable userA = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }
        Runnable userA = () ->{
            fieldService.logic("userA");
        };
        Runnable userB = () ->{
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        sleep(2000); //동시성 문제 발생 X
        threadB.start();
        sleep(2000); //메인 쓰레드 종료 대기
    }
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
