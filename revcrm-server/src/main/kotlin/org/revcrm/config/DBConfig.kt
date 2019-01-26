package org.revcrm.config

import java.lang.IllegalArgumentException

class DBConfig(
    val dbUrl: String,
    val dbName: String,
    val entityClasses: List<String>,
    val embeddedClasses: List<String>
) {
    private val decimalPrecision: MutableMap<String, Int> = mutableMapOf(
        "InvoiceUnitPrice" to 2,
        "InvoiceQuantity" to 2,
        "InvoiceTotal" to 2
    )

    fun getPrecision(precisionName: String): Int {
        val precision = decimalPrecision.get(precisionName)
        if (precision == null)
            throw IllegalArgumentException("Precision name '$precisionName' is not configured.")
        return precision
    }
}