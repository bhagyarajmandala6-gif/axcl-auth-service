package com.innocito.axcl.enums;

import com.innocito.axcl.exception.EnumNotFoundException;

import java.util.Arrays;
import java.util.List;

public enum UserType {
    USER("User", 1), TP_ADMIN("TPAdmin", 2),
    DRIVER("Driver", 3), SUPER_ADMIN("SuperAdmin", 4);
    private final String key;
    private final int value;

    UserType(String key, int value) {
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
        return Arrays.stream(values()).map(UserType::getKey).toList();
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(UserType::getValue).toList();
    }

    public static UserType getByKey(String key) {
        for (UserType type : values()) {
            if (type.getKey().equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new EnumNotFoundException("user type", "user type" + "not found with " + key);
    }

    public static UserType getByValue(int value) {
        for (UserType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new EnumNotFoundException("user type", "user type" + "not found with " + value);
    }
}