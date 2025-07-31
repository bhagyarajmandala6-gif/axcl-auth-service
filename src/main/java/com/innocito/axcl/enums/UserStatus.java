package com.innocito.axcl.enums;

import com.innocito.axcl.exception.EnumNotFoundException;

import java.util.Arrays;
import java.util.List;

public enum UserStatus {
    ACTIVE("ACTIVE", 1), SUSPENDED("SUSPENDED", 2),
    INACTIVE("INACTIVE", 3);
    private final String key;
    private final int value;

    UserStatus(String key, int value) {
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
        return Arrays.stream(values()).map(UserStatus::getKey).toList();
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(UserStatus::getValue).toList();
    }

    public static UserStatus getByKey(String key) {
        for (UserStatus type : values()) {
            if (type.getKey().equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new EnumNotFoundException("user status", "user status" + "not found with " + key);
    }

    public static UserStatus getByValue(int value) {
        for (UserStatus type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new EnumNotFoundException("user status", "user status" + "not found with " + value);
    }
}