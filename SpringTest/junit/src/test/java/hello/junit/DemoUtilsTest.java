package hello.junit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void setupBeforeEach() {
        demoUtils = new DemoUtils();
    }


    @Test
    @DisplayName("Equals and Not Equals")
    void testEqualsAndNotEquals() {

        assertThat(6).isEqualTo(demoUtils.add(2, 4));
        assertThat(demoUtils.add(1, 9)).isNotEqualTo(6);
    }

    @Test
    @DisplayName("Null and Not Null")
    void testNullAndNotNull() {
        String str1 = null;
        String str2 = "test unit";

        assertThat(demoUtils.checkNull(str1)).isNull();
        assertThat(demoUtils.checkNull(str2)).isNotNull();

    }

    @Test
    @DisplayName("Same and Not Same")
    void testSameAndNotSame() {
        String str = "luv2code";
        assertThat(demoUtils.getAcademy()).isSameAs(demoUtils.getAcademyDuplicate());
        assertThat(demoUtils.getAcademy()).isNotSameAs(str);
    }

    @Test
    @DisplayName("True and False")
    void testTrueFalse() {
        int gradeOne = 10;
        int gradeTwo = 5;
        assertThat(demoUtils.isGreater(gradeOne, gradeTwo)).isTrue();
        assertThat(demoUtils.isGreater(gradeTwo, gradeOne)).isFalse();
    }

    @Test
    @DisplayName("Array Equals")
    void testArrayEquals() {
        String[] stringArray = {"A", "B", "C"};
        assertThat(stringArray).isEqualTo(demoUtils.getFirstThreeLettersOfAlphabet());
    }

    @Test
    @DisplayName("Iterable Equals")
    void testIterableEquals() {
        List<String> theList = List.of("luv", "2", "code");
        assertThat(theList).isEqualTo(demoUtils.getAcademyInList());
    }

    @Test
    @DisplayName("Throws and Does Not Throw")
    void testThrowsAndDoesNotThrow() {
        assertThatThrownBy(() -> demoUtils.throwException(-1))
                .isInstanceOf(Exception.class);
        assertThatCode(()-> demoUtils.throwException(1))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Timeout")
    void testTimeout() {
        // 3초보다 checkTimeout가 작게 실행시 성공
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(3),
                () -> demoUtils.checkTimeout());
    }



/*
    @AfterEach
    void tearDownAfterEach() {
        log.info("Running @AfterEach");
        log.info("-------------------");
    }

    @BeforeAll
    static void setupBeforeAll() {
        log.info("@BeforeAll 테스트 메서드를 시작하기 전에 한번만 실행됩니다.");
    }

    @AfterAll
    static void tearDownAfterAll() {
        log.info("@AfterAll 테스트 메서드를 시작한 후에 한번만 실행됩니다.");
    }
    */

}