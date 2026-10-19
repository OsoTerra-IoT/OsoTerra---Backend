package com.osoterra.ososense;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Translates the {@code DATABASE_URL} that hosting platforms such as Railway inject
 * ({@code postgresql://user:password@host:port/database}) into the {@code DB_URL},
 * {@code DB_USERNAME} and {@code DB_PASSWORD} values the application reads. Explicit
 * {@code DB_*} settings always win.
 */
final class PlatformDatabaseUrl {

    private PlatformDatabaseUrl() {
    }

    static Map<String, String> toDatasourceSettings(Map<String, String> environment) {
        Map<String, String> settings = new LinkedHashMap<>();
        String databaseUrl = environment.get("DATABASE_URL");
        if (environment.get("DB_URL") != null || databaseUrl == null
                || !databaseUrl.matches("^postgres(ql)?://.+")) {
            return settings;
        }
        URI uri = URI.create(databaseUrl);
        int port = uri.getPort() == -1 ? 5432 : uri.getPort();
        settings.put("DB_URL", "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath());
        String userInfo = uri.getRawUserInfo();
        if (userInfo != null) {
            int separator = userInfo.indexOf(':');
            String user = separator < 0 ? userInfo : userInfo.substring(0, separator);
            settings.put("DB_USERNAME", decode(user));
            if (separator >= 0) {
                settings.put("DB_PASSWORD", decode(userInfo.substring(separator + 1)));
            }
        }
        return settings;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
