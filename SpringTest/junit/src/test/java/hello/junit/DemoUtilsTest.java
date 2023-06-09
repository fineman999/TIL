package hello.junit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void setupBeforeEach() {
        demoUtils = new DemoUtils();
        log.info("@BeforeEach executes before the execution of each test method");
    }

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




    @Test
    void testEqualsAndNotEquals() {
        log.info("Running test: testEqualsAndNotEquals");


        assertThat(6).isEqualTo(demoUtils.add(2, 4));
        assertThat(demoUtils.add(1, 9)).isNotEqualTo(6);
    }

    @Test
    void testNullAndNotNull() {
        log.info("Running test: testNullAndNotNull");

        String str1 = null;
        String str2 = "test unit";

        assertThat(demoUtils.checkNull(str1)).isNull();
        assertThat(demoUtils.checkNull(str2)).isNotNull();

    }

}