package io.chan.serializer;

import static org.junit.jupiter.api.Assertions.*;

import io.chan.Generator;
import io.chan.Laptop;
import org.junit.jupiter.api.Test;

class SerializerTest {
    
    @Test
    void writeAndReadBinaryFile() {
        String binaryFile = "laptop.bin";
        final Laptop laptop = new Generator().newLaptop();

        Serializer serializer = new Serializer();
        serializer.WriteBinaryFile(laptop, binaryFile);

        Laptop readLaptop = serializer.ReadBinaryFile(binaryFile);
        assertEquals(laptop, readLaptop);
    }
}
