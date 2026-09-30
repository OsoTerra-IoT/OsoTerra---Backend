package com.osoterra.ososense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class OsosenseBackendApplication {

    public static void main(String[] args) {
        loadDotenv();
        SpringApplication.run(OsosenseBackendApplication.class, args);
    }

    /**
     * Loads local-only overrides from a {@code .env} file in the working directory into
     * JVM system properties, so Spring's {@code ${VAR:default}} placeholders pick them up.
     * A real OS environment variable of the same name always wins. Absent in every
     * environment that sets its own environment variables (CI, production) — the file is
     * gitignored and only expected on a developer's machine.
     */
    private static void loadDotenv() {
        Path envFile = Path.of(".env");
        if (!Files.exists(envFile)) {
            return;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(envFile);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read .env", e);
        }
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            int separator = trimmed.indexOf('=');
            if (separator <= 0) {
                continue;
            }
            String key = trimmed.substring(0, separator).trim();
            String value = trimmed.substring(separator + 1).trim();
            if (System.getenv(key) == null && System.getProperty(key) == null) {
                System.setProperty(key, value);
            }
        }
    }
}
