package hello.tdd;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FizzBuzzTest {

    // 숫자가 3의 배수이면 Fizz를 출력
    @DisplayName("Divisible by Tree")
    @Test
    @Order(1)
    void testForDivisibleByTree() {
        String expected = "Fizz";

        assertThat(expected).isEqualTo(FizzBuzz.compute(3));
    }

    // 숫자가 5의 배수이면 Buzz를 출력
    @DisplayName("Divisible by Five")
    @Test
    @Order(2)
    void testForDivisibleByFive() {
        String expected = "Buzz";
        assertThat(expected).isEqualTo(FizzBuzz.compute(5));
    }


    // 숫자가 3과 5의 배수이면 FizzBuzz를 출력
    @DisplayName("Divisible by Three and Five")
    @Test
    @Order(3)
    void testForDivisibleByThreeAndFive() {
        String expected = "FizzBuzz";
        assertThat(expected).isEqualTo(FizzBuzz.compute(15));
    }

    // 숫자가 3과 5의 배수가 아니면 숫자 출력
    @DisplayName("Not Divisible by Three and Five")
    @Test
    @Order(3)
    void testForNotDivisibleByThreeAndFive() {
        String expected = "1";

        assertThat(expected).isEqualTo(FizzBuzz.compute(1));
    }

    @DisplayName("Testing with Small data file")
    @ParameterizedTest(name = "value={0}, expected={1}")
    @CsvFileSource(resources = "/small-test-data.csv")
    @Order(4)
    void testSmallDataFile(int value, String expected) {
        assertThat(expected).isEqualTo(FizzBuzz.compute(value));
    }



}
