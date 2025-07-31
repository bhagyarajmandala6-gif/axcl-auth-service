package com.innocito.axcl.enums;

import com.innocito.axcl.exception.EnumNotFoundException;

import java.util.Arrays;
import java.util.List;

public enum DmvLicensePlateCategory {
    NON_COMMERCIAL("Non-Commercial", 1), COMMERCIAL_LIVERY_DOT("Commercial - Livery (DOT)", 2),
    COMMERCIAL_TLC("Commercial - TLC", 3),COMMERCIAL_BUS_DOT("Commercial - BUS (DOT)", 4),
    COMMERCIAL_AMBULANCE("Commercial - Ambulance", 5);
    private final String key;
    private final int value;

    DmvLicensePlateCategory(String key, int value) {
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
        return Arrays.stream(values()).map(DmvLicensePlateCategory::getKey).toList();
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(DmvLicensePlateCategory::getValue).toList();
    }

    public static DmvLicensePlateCategory getByKey(String key) {
        for (DmvLicensePlateCategory type : values()) {
            if (type.getKey().equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new EnumNotFoundException("DmvLicensePlateCategory", "DmvLicensePlateCategory" + " not found with " + key);
    }

    public static DmvLicensePlateCategory getByValue(int value) {
        for (DmvLicensePlateCategory type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new EnumNotFoundException("DmvLicensePlateCategory", "DmvLicensePlateCategory" + " not found with " + value);
    }

    public static DmvLicensePlateCategory getByName(String name) {
        for (DmvLicensePlateCategory type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new EnumNotFoundException("DmvLicensePlateCategory", "DmvLicensePlateCategory" + " not found with " + name);
    }
}
