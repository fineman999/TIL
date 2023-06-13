package hello.testtheory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CalculationRequestTest {

    @Test
    @DisplayName(" 유효한 숫자를 파싱할 수 있다.")
    void validNumTest() {
        // given
        String[] parts = {"2", "+", "3"};

        // when
        CalculationRequest calculationRequest = new CalculationRequest(parts);

        // then
        assertThat(calculationRequest.getNum1()).isEqualTo(2);
        assertThat(calculationRequest.getOperator()).isEqualTo("+");
        assertThat(calculationRequest.getNum2()).isEqualTo(3);

    }
    @Test
    @DisplayName("세자리 숫자가 넘어가는 유효한 숫자를 파싱할 수 있다.")
    void validLengthTest() {
        // given
        String[] parts = {"232", "+", "123"};

        // when
        CalculationRequest calculationRequest = new CalculationRequest(parts);

        // then
        assertThat(calculationRequest.getNum1()).isEqualTo(232);
        assertThat(calculationRequest.getOperator()).isEqualTo("+");
        assertThat(calculationRequest.getNum2()).isEqualTo(123);
    }

    @Test
    @DisplayName("유효한 길이의 숫자가 들어오지 않으면 에러를 던진다..")
    void notValidLengthTest() {
        // given
        String[] parts = {"232", "+"};

        // when
        assertThatThrownBy(() -> new CalculationRequest(parts))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("유효하지 않는 연산자가 들어오면 에러를 던진다.")
    void notValidOperator() {
        // given
        String[] parts = {"232", "x", "2"};

        // when
        // then
        assertThatThrownBy(() -> new CalculationRequest(parts))
                .isInstanceOf(InvalidOperatorException.class);
    }
    @Test
    @DisplayName("유효하지 않는 길이 연산자가 들어오면 에러를 던진다.")
    void notValidOperatorLength() {
        // given
        String[] parts = {"232", "xx", "2"};

        // when
        // then
        assertThatThrownBy(() -> new CalculationRequest(parts))
                .isInstanceOf(InvalidOperatorException.class);
    }






}
