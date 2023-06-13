package hello.testtheory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CalculatorTest {

    @Test
    @DisplayName("덧셈 연산을 할 수 있다.")
    void temp() {
        // given
        long num1 = 2;
        String operation = "+";
        long num2 = 3;
        Calculator calculator = new Calculator();

        // when
        long result = calculator.calculate(num1, operation, num2);

        // then
        assertThat(result).isEqualTo(5);
    }

    @Test
    @DisplayName("곱셈 연산을 할 수 있다.")
    void multiTest() {
        // given
        long num1 = 2;
        String operation = "*";
        long num2 = 3;
        Calculator calculator = new Calculator();

        // when
        long result = calculator.calculate(num1, operation, num2);

        // then
        assertThat(result).isEqualTo(6);
    }

    @Test
    @DisplayName("뺄셈 연산을 할 수 있다.")
    void minusTest() {
        // given
        long num1 = 2;
        String operation = "-";
        long num2 = 3;
        Calculator calculator = new Calculator();

        // when
        long result = calculator.calculate(num1, operation, num2);

        // then
        assertThat(result).isEqualTo(-1);
    }

    @Test
    @DisplayName("나눗셈 연산을 할 수 있다.")
    void divideTest() {
        // given
        long num1 = 6;
        String operation = "/";
        long num2 = 3;
        Calculator calculator = new Calculator();

        // when
        long result = calculator.calculate(num1, operation, num2);

        // then
        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("잘못된 연산자 입력시 예외 발생을 할 수 있다.")
    void operatorExceptionTest() {
        // given
        long num1 = 6;
        String operation = "**";
        long num2 = 3;
        Calculator calculator = new Calculator();

        // when
        // then
        assertThatThrownBy(() -> calculator.calculate(num1, operation, num2))
                .isInstanceOf(InvalidOperatorException.class);


    }


}