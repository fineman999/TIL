package io.chan.userservice.setup;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(DBInitializer.class)
public abstract class ApplicationTest extends Testcontainers{

    @Autowired
    private DBInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        dataInitializer.clear();
    }
}
