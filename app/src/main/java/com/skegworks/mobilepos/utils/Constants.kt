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

    class Invoice {
        companion object {
            val infoForCustomerDefault = arrayOf(
                " - Thank you for shopping with us!",
                " - വിറ്റ സാധനങ്ങൾ 5 ദിവസത്തിനകം മാറ്റിയെടുക്കാവുന്നതാണ്, പണം തിരികെ നൽകുന്നതല്ല.",
                " - സാധനം കേടുപാട് കൂടാതെയും ഒറിജിനൽ ടാഗുമായും കൊണ്ടുവരണം.",
                " - വസ്ത്രങ്ങൾ ആദ്യപ്രാവശ്യം ഡ്രൈ വാഷ് ചെയുക.",
                " - കോട്ടൺ വസ്ത്രങ്ങൾ ചെറിയ രീതിയിൽ ചുരുങ്ങാനും കളർ മങ്ങാനും സാധ്യതയുണ്ട്.",
                " - ഫാൻസി ഐറ്റംസ്, ചപ്പൽസ്, ഡിസ്കൗണ്ട് ഐറ്റംസ് തിരിച്ചെടുക്കുന്നതല്ല.",
            )

            val infoForCustomerPremium = arrayOf(
                " - Thank you for shopping with us!",
                " - DRY WASH ONLY!",
                " - വിറ്റ സാധനങ്ങൾ 5 ദിവസത്തിനകം മാറ്റിയെടുക്കാവുന്നതാണ്, പണം തിരികെ നൽകുന്നതല്ല.",
                " - സാധനം കേടുപാട് കൂടാതെയും ഒറിജിനൽ ടാഗുമായും കൊണ്ടുവരണം.",
                " - വസ്ത്രങ്ങൾ ആദ്യപ്രാവശ്യം ഡ്രൈ വാഷ് ചെയുക.",
                " - കോട്ടൺ വസ്ത്രങ്ങൾ ചെറിയ രീതിയിൽ ചുരുങ്ങാനും കളർ മങ്ങാനും സാധ്യതയുണ്ട്.",
            )
        }
    }

    class DateFormat {
        companion object {
            const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
            const val INVOICE_DATE_TIME_FORMAT = "dd/M/yyyy HH:mm"
            const val DATE_ONLY_FORMAT = "dd/M/yyyy"
        }
    }
}