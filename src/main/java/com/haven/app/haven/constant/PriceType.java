package com.haven.app.haven.constant;

public enum PriceType {
    WNI("NIK"),
    WNA("PASSPORT");

    private String type;

    PriceType(String type) {
        this.type = type;
    }

    public static PriceType getPriceType(String type) {
        for (PriceType priceType : PriceType.values()) {
            if (priceType.type.equals(type)) {
                return priceType;
            }
        }
        return null;
    }
}
