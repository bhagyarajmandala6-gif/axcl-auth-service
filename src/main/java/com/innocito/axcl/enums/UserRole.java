package com.innocito.axcl.enums;

import com.innocito.axcl.exception.EnumNotFoundException;

import java.util.Arrays;
import java.util.List;

public enum UserRole {
    RIDER("RIDER", 1), TRANSPORT_PROVIDER("TRANSPORT_PROVIDER", 2),
    DRIVER("DRIVER", 3), ADMIN("ADMIN", 4);
    private final String key;
    private final int value;

    UserRole(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public static List<String> getKeys() {
        return Arrays.stream(values()).map(UserRole::getKey).toList();
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(UserRole::getValue).toList();
    }

    public static UserRole getByKey(String key) {
        for (UserRole type : values()) {
            if (type.getKey().equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new EnumNotFoundException("user role", "user role" + "not found with " + key);
    }

    public static UserRole getByValue(int value) {
        for (UserRole type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new EnumNotFoundException("user role", "user role" + "not found with " + value);
    }
}