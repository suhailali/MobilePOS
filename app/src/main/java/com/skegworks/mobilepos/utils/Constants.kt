package com.skegworks.mobilepos.utils

class Constants {
    class FirebaseDocument {
        companion object {
            const val APP_SETTINGS = "app_settings"
            const val BUSINESS = "business"
            const val CATEGORIES = "categories"
            const val PRODUCTS = "products"
            const val CUSTOMERS = "customers"
            const val VENDORS = "vendors"
            const val INVOICES = "invoices"
            const val INVOICE_ITEMS = "invoices_items"
            const val COUPONS = "coupons"
            const val CASH_COUNTER = "cash_counter"
            const val CREDIT_NOTES = "credit_notes"
            const val CREDIT_NOTE_ITEMS = "credit_note_items"
        }
    }

    class FirebaseCollection {
        companion object {
            const val APP = "cash_counter"
        }
    }

    class DateFormat {
        companion object {
            const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        }
    }
}