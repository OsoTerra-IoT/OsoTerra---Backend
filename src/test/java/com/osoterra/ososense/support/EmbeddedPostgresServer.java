package com.osoterra.ososense.support;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.springframework.test.context.DynamicPropertyRegistry;

/**
 * Starts a single throwaway PostgreSQL server for the whole test run so integration tests
 * exercise the real Flyway migrations without Docker or local credentials.
 */
public final class EmbeddedPostgresServer {

    private static EmbeddedPostgres server;

    private EmbeddedPostgresServer() {
    }

    public static synchronized void register(DynamicPropertyRegistry registry) {
        if (server == null) {
            try {
                server = EmbeddedPostgres.start();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
        registry.add("spring.datasource.url", () -> server.getJdbcUrl("postgres", "postgres"));
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "postgres");
    }
}
