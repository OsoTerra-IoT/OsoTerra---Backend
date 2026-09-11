package com.osoterra.ososense;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requires a running PostgreSQL instance; enable once a test database or Testcontainers is wired up")
class OsosenseBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
