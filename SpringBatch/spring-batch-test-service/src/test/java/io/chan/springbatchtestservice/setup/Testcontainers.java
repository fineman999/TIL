package io.chan.springbatchtestservice.setup;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

public abstract class Testcontainers {
    private static final String MYSQL_USERNAME = "test";
    private static final String MYSQL_PASSWORD = "1234";

    public static MySQLContainer<?> mysqlContainer = createContainer();


    private static MySQLContainer<?> createContainer() {
        @SuppressWarnings("resource")
        MySQLContainer<?> container = new MySQLContainer<>("mysql:8")
                .withDatabaseName("test")
                .withUsername(MYSQL_USERNAME)
                .withPassword(MYSQL_PASSWORD)
                .withReuse(true);

        container.start();
        return container;
    }

    @DynamicPropertySource
    private static void configureProperties(final DynamicPropertyRegistry registry) {
        Startables.deepStart(mysqlContainer).join(); // 컨테이너가 여러개일시 병렬로 실행(기존은 순차적)


        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", () -> MYSQL_USERNAME);
        registry.add("spring.datasource.password", () -> MYSQL_PASSWORD);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.jpa.show-sql", () -> "true");
    }
}
