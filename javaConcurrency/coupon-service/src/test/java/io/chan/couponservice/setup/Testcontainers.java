package io.chan.couponservice.setup;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class Testcontainers {
    private static final String MYSQL_USERNAME = "test";
    private static final String MYSQL_PASSWORD = "1234";

    public static MySQLContainer<?> mysqlContainer = createContainer();
    public static GenericContainer<?> redisContainer = createRedisContainer();

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
    private static GenericContainer<?> createRedisContainer() {
        @SuppressWarnings("resource")
        GenericContainer<?> redisContainer = new GenericContainer<>(
                DockerImageName.parse("redis:7.0.8-alpine"))
                .withExposedPorts(6379)
                .withCommand("redis-server", "--requirepass password")
                .withReuse(true);
        redisContainer.start();
        return redisContainer;
    }

    @DynamicPropertySource
    private static void configureProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", () -> MYSQL_USERNAME);
        registry.add("spring.datasource.password", () -> MYSQL_PASSWORD);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.jpa.show-sql", () -> "true");

        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379).toString());
        registry.add("spring.data.redis.password", () -> "password");
        registry.add("spring.data.redis.repositories.enabled", () -> "false");
    }
}
