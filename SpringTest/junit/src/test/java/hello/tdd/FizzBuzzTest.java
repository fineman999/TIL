package hello.tdd;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Fail.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FizzBuzzTest {
    // 숫자가 3의 배수이면 Fizz를 출력
    // 숫자가 5의 배수이면 Buzz를 출력
    // 숫자가 3과 5의 배수이면 FizzBuzz를 출력
    // 숫자가 3과 5의 배수가 아니면 숫자 출력

    @DisplayName("Divisible by Tree")
    @Test
    @Order(1)
    void testForDivisibleByTree() {
        fail("fail");
    }
}
