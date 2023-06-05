package memory;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemoryFinderTest {

    @Test
    void get() {
        MemoryFinder memoryFinder = new MemoryFinder();
        Memory memory = memoryFinder.get();
        System.out.println("memory = " + memory);
        assertThat(memory).isNotNull();
    }
}
