package org.revcrm.config

import java.lang.IllegalArgumentException

class AppConfig {
    private val decimalScale: MutableMap<String, Int> = mutableMapOf(
        "InvoiceUnitPrice" to 2,
        "InvoiceQuantity" to 2,
        "InvoiceTotal" to 2
    )

    fun getDecimalScale(name: String): Int {
        val scale = decimalScale.get(name)
        if (scale == null)
            throw IllegalArgumentException("Decimal Scale '$name' is not configured.")
        return scale
    }
}