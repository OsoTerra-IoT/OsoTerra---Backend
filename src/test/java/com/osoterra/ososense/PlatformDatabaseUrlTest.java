package com.osoterra.ososense;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class PlatformDatabaseUrlTest {

    @Test
    void translatesRailwayDatabaseUrlIntoJdbcSettings() {
        Map<String, String> settings = PlatformDatabaseUrl.toDatasourceSettings(Map.of(
                "DATABASE_URL", "postgresql://postgres:s3cr%40t@postgres.railway.internal:5432/railway"));

        assertThat(settings).containsExactly(
                Map.entry("DB_URL", "jdbc:postgresql://postgres.railway.internal:5432/railway"),
                Map.entry("DB_USERNAME", "postgres"),
                Map.entry("DB_PASSWORD", "s3cr@t"));
    }

    @Test
    void keepsExplicitDatasourceSettings() {
        assertThat(PlatformDatabaseUrl.toDatasourceSettings(Map.of(
                "DATABASE_URL", "postgresql://u:p@host:5432/db",
                "DB_URL", "jdbc:postgresql://other:5432/db"))).isEmpty();
    }

    @Test
    void ignoresMissingOrNonPostgresUrls() {
        assertThat(PlatformDatabaseUrl.toDatasourceSettings(Map.of())).isEmpty();
        assertThat(PlatformDatabaseUrl.toDatasourceSettings(Map.of("DATABASE_URL", "mysql://u:p@h/db"))).isEmpty();
    }
}
