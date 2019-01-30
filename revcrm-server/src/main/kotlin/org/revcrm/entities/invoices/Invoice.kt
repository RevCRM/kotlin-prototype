package org.revcrm.entities.invoices

import org.revcrm.annotations.EmbeddedEntity
import org.revcrm.annotations.Label
import org.revcrm.annotations.Stored
import org.revcrm.entities.Base
import org.revcrm.entities.BaseEmbedded
import org.revcrm.entities.accounts.Account
import xyz.morphia.annotations.Embedded
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Reference
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

class TaxRate(
    percentage: Double
) {
    private val multiplier = BigDecimal(percentage).multiply(BigDecimal(0.01))

    fun getTaxAmount(
        value: BigDecimal,
        decimalScale: Int = 2,
        roundingMode: RoundingMode = RoundingMode.DOWN
    ): BigDecimal {
        return (value * multiplier).setScale(decimalScale, roundingMode)
    }
}

val TAX_RATE = TaxRate(15.0)

@EmbeddedEntity
class InvoiceLine(

    @Label("Item") @field:NotBlank
    var item: String,

    @Label("Quantity") @field:Positive
    var quantity: BigDecimal,

    @Label("Unit") @field:NotBlank
    var unit: String,

    @Label("Unit Price") @field:PositiveOrZero
    var unit_price: BigDecimal,

    @Label("Line Discount") @field:PositiveOrZero
    var discount_amount: BigDecimal
)
    : BaseEmbedded() {

    @Label("Quantity Price")
    @Stored
    val quantity_price: BigDecimal
        get() = quantity * unit_price

    @Label("Net Total")
    @Stored
    val net_total: BigDecimal
        get() {
            val scale = this.context!!.config.getDecimalScale("InvoiceTotal")
            return (quantity_price - discount_amount).setScale(scale, RoundingMode.UP)
        }

    @Label("Line Tax")
    @Stored
    val line_tax: BigDecimal
        get() {
            val precision = this.context!!.config.getDecimalScale("InvoiceTotal")
            return TAX_RATE.getTaxAmount(net_total, precision)
        }

    @Label("Gross Total")
    @Stored
    val line_total: BigDecimal
        get() = net_total + line_tax
}

@Entity
class Invoice(

    @Label("Account")
    @Reference
    var account: Account,

    @Label("Invoice Number")
    var invoice_number: String?,

    @Label("Invoice Date")
    var invoice_date: LocalDate,

    @Label("Currency")
    var invoice_currency: String,

    @Label("Payment Due Date")
    var payment_due_date: LocalDate,

    @Embedded
    @Label("Invoice Lines")
    @field:Size(min = 1) @field:Valid
    var lines: List<InvoiceLine>
)
    : Base() {

    @Label("Invoice Net Total")
    @Stored
    val invoice_net_total: BigDecimal
        get() = lines.map { it.net_total }
            .fold(BigDecimal.ZERO, BigDecimal::add)

    @Label("Invoice Tax Total")
    @Stored
    val invoice_tax_total: BigDecimal
        get() = lines.map { it.line_tax }
            .fold(BigDecimal.ZERO, BigDecimal::add)

    @Label("Total Discount")
    @Stored
    val invoice_discount_amount: BigDecimal
        get() = lines.map { it.discount_amount }
            .fold(BigDecimal.ZERO, BigDecimal::add)

    @Label("Invoice Total")
    @Stored
    val invoice_total: BigDecimal
        get() = lines.map { it.line_total }
            .fold(BigDecimal.ZERO, BigDecimal::add)
}