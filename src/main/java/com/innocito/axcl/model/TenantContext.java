package com.innocito.axcl.model;

public class TenantContext {
    private static final ThreadLocal<TenantData> tenantDataThreadLocal = new ThreadLocal<>();

    public static TenantData getTenantData() {
        return tenantDataThreadLocal.get();
    }

    public static void setTenantData(TenantData tenantData) {
        tenantDataThreadLocal.set(tenantData);
    }

    public static void clear() {
        tenantDataThreadLocal.remove();
    }
}
