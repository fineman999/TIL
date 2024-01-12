package org.example.exam05.single_n_multi;

public class SingleThreadExample {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
            try {
                Thread.sleep(1);
                // 여기서 예외가 발생하면 애플리케이션이 종료된다.
//                throw new RuntimeException("예외 발생");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 합계: 499500
        //소요 시간: 1240ms
        System.out.printf("합계: %d\n", sum);
        System.out.printf("소요 시간: %dms\n", System.currentTimeMillis() - start);
    }
}
