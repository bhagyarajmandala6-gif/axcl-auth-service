package com.innocito.axcl.enums;

import com.innocito.axcl.exception.EnumNotFoundException;

import java.util.Arrays;
import java.util.List;

public enum VehicleType {
    Livery("Livery", 1), Ambulette("Ambulette", 3),
    Ambulance("Ambulance", 6);
    private final String key;
    private final int value;

    VehicleType(String key, int value) {
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
        return Arrays.stream(values()).map(VehicleType::getKey).toList();
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(VehicleType::getValue).toList();
    }

    public static VehicleType getByKey(String key) {
        for (VehicleType type : values()) {
            if (type.getKey().equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new EnumNotFoundException("vehicle type", "vehicle type" + "not found with " + key);
    }

    public static VehicleType getByValue(int value) {
        for (VehicleType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new EnumNotFoundException("vehicle type", "vehicle type" + "not found with " + value);
    }

    public static VehicleType getByName(String name) {
        for (VehicleType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new EnumNotFoundException("vehicle type", "vehicle type" + "not found with " + name);
    }
}
