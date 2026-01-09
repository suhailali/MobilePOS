package com.skegworks.mobilepos.coupon

enum class CouponType(val type: String) {
    FLAT_PERCENTAGE("PERCENTAGE"),
    FLAT_AMOUNT("FLAT_AMOUNT"),
    SELECTED_ITEM_PERCENTAGE("SELECTED_ITEM_PERCENTAGE"),
    SELECTED_ITEM_AMOUNT("SELECTED_ITEM_AMOUNT");

    companion object {
        fun fromType(type: String): CouponType? {
            return CouponType.entries.firstOrNull() { it.type.equals(type, ignoreCase = true) }
        }
    }
}