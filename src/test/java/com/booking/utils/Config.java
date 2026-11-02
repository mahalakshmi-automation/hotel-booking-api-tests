package com.booking.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
    private static final Properties VALUES = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("Missing config.properties");
            }
            VALUES.load(input);
        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private Config() {
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(key.toUpperCase().replace('.', '_'));
        }
        if (value == null || value.isBlank()) {
            value = VALUES.getProperty(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing configuration: " + key);
        }
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
