package io.hhplus.concert.app.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@ActiveProfiles("test")
@EnableAsync
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class TestContainerSupport {

    private static final JdbcDatabaseContainer MYSQL = new MySQLContainer("mysql:8");

    private static final GenericContainer REDIS = new GenericContainer("redis:6.2.6").withExposedPorts(6379);

    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @BeforeAll
    public static void startContainers() {
        log.info("Starting containers...");
        MYSQL.start();
        REDIS.start();
        KAFKA_CONTAINER.start();
        log.info("Containers started successfully.");
    }

    @AfterAll
    public static void stopContainers() {
        log.info("Stopping containers...");
        MYSQL.stop();
        REDIS.stop();
        log.info("Containers stopped successfully.");
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        // mysql
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.~username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);

        // redis
        registry.add("spring.data.redis.host", REDIS::getHost);
        registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(6379));

        // kafka
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }

    static {
        MYSQL.waitingFor(Wait.forListeningPort());
        REDIS.waitingFor(Wait.forListeningPort());
        KAFKA_CONTAINER.waitingFor(Wait.forListeningPort());
    }
}