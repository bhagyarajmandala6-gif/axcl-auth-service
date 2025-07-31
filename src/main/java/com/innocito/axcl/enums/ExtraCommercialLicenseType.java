package com.innocito.axcl.enums;

import com.innocito.axcl.exception.EnumNotFoundException;

import java.util.Arrays;
import java.util.List;

public enum ExtraCommercialLicenseType {
    FHV("For-Hire Vehicle (FHV)", 1), PR("Paratransit (PR)", 2),
    EMT("EMT", 3);
    private final String key;
    private final int value;

    ExtraCommercialLicenseType(String key, int value) {
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
        return Arrays.stream(values()).map(ExtraCommercialLicenseType::getKey).toList();
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(ExtraCommercialLicenseType::getValue).toList();
    }

    public static ExtraCommercialLicenseType getByKey(String key) {
        for (ExtraCommercialLicenseType type : values()) {
            if (type.getKey().equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new EnumNotFoundException("ExtraCommercialLicenseType", "ExtraCommercialLicenseType" + "not found with " + key);
    }

    public static ExtraCommercialLicenseType getByValue(int value) {
        for (ExtraCommercialLicenseType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new EnumNotFoundException("ExtraCommercialLicenseType", "ExtraCommercialLicenseType" + "not found with " + value);
    }

    public static ExtraCommercialLicenseType getByName(String name) {
        for (ExtraCommercialLicenseType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new EnumNotFoundException("ExtraCommercialLicenseType", "ExtraCommercialLicenseType" + "not found with " + name);
    }
}
