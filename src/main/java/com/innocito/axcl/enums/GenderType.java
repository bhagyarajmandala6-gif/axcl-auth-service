package com.innocito.axcl.enums;

import com.innocito.axcl.exception.EnumNotFoundException;

import java.util.Arrays;
import java.util.List;

public enum GenderType {
    M("Male", 1), F("Female", 2),
    O("Others", 3), U("Prefer not to say", 4);
    private final String key;
    private final int value;

    GenderType(String key, int value) {
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
        return Arrays.stream(values()).map(GenderType::getKey).toList();
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(GenderType::getValue).toList();
    }

    public static GenderType getByKey(String key) {
        for (GenderType type : values()) {
            if (type.getKey().equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new EnumNotFoundException("gender type", "gender type" + "not found with " + key);
    }

    public static GenderType getByValue(int value) {
        for (GenderType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new EnumNotFoundException("gender type", "gender type" + "not found with " + value);
    }

    public static GenderType getByName(String name) {
        for (GenderType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new EnumNotFoundException("gender type", "gender type" + "not found with " + name);
    }
}
