package hello.junit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

public class ConditionalTest {

    @Test
    @Disabled("Don't run until JIRA #123 is resolved")
    void basicTest() {
        // execute method and perform asserts
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testForWindowsOnly() {
        // execute method and perform asserts
    }


    @Test
    @EnabledOnOs(OS.MAC)
    void testForMacOnly() {
        // execute method and perform asserts
    }
    @Test
    @EnabledOnOs({OS.MAC, OS.WINDOWS})
    void testForMacAndWindowsOnly() {
        // execute method and perform asserts
    }

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void testForOnlyForJava17() {
        // execute method and perform asserts
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void testForOnlyForJava8() {
        // execute method and perform asserts
    }

    @Test
    @EnabledForJreRange(min=JRE.JAVA_8, max = JRE.JAVA_20)
    void testForOnlyForJavaRange() {
        // execute method and perform asserts
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "true")
    void testForOnlyForDevEnvironment() {
        // execute method and perform asserts
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "PROD")
    void testForOnlyForProdEnvironment() {
        // execute method and perform asserts
    }



}
