package tobyspring.helloboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@HellobootTest
public class HelloServiceCountTest {

    @Autowired HelloService helloService;
    @Autowired HelloRepository helloRepository;

    @Test
    void sayHellIncreaseCount() {
        IntStream.rangeClosed(1, 10).forEach(count -> {
            helloService.sayHello("Toby");
            assertThat(helloRepository.countOf("Toby")).isEqualTo(count);
        });
    }
}
