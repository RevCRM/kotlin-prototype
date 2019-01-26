package org.revcrm.entities.invoices

import org.revcrm.annotations.EmbeddedEntity
import org.revcrm.annotations.Label
import org.revcrm.entities.Base
import org.revcrm.entities.BaseEmbedded
import xyz.morphia.annotations.Embedded
import xyz.morphia.annotations.Entity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

class TaxRate(
    val percentage: Double
) {
    private val multiplier = BigDecimal(percentage).multiply(BigDecimal(0.01))

    fun getTaxAmount(
        value: BigDecimal,
        precision: Int = 2,
        roundingMode: RoundingMode = RoundingMode.DOWN
    ): BigDecimal {
        val mathCtx = MathContext(precision, roundingMode)
        return value.multiply(multiplier, mathCtx)
    }
}

val TAX_RATE = TaxRate(15.0)

@EmbeddedEntity
class InvoiceLine(

    @Label("Item") @field:NotBlank
    var item: String,

    @Label("Quantity") @field:Positive
    var quantity: BigDecimal,

    @Label("Unit Price") @field:PositiveOrZero
    var unit_price: BigDecimal,

    @Label("Line Discount") @field:PositiveOrZero
    var discount_amount: BigDecimal
)
    : BaseEmbedded() {
    @Label("Quantity Price")
    val quantity_price: BigDecimal
        get() = quantity * unit_price

    @Label("Net Total")
    val net_total: BigDecimal
        get() = quantity_price - discount_amount

    @Label("Line Tax")
    val line_tax: BigDecimal
        get() {
            val precision = this.context!!.config.getPrecision("InvoiceTotal")
            return TAX_RATE.getTaxAmount(net_total, precision)
        }

    @Label("Gross Total")
    val gross_total: BigDecimal
        get() = net_total + line_tax
}

@Entity
class Invoice(

//    @Reference
//    @Label("Account")
//    var account: Account,

    @Label("Invoice Number")
    var invoice_number: String?,

    @Label("Invoice Date")
    var invoice_date: LocalDate,

    @Label("Currency")
    var invoice_currency: String,

    @Embedded
    @Label("Invoice Lines") @field:Size(min = 1) @field:Valid
    var lines: List<InvoiceLine>,

    @Label("Invoice Total") @field:PositiveOrZero
    var invoice_total: BigDecimal,

    @Label("Invoice Net Total") @field:PositiveOrZero
    var invoice_net_total: BigDecimal,

    @Label("Invoice Tax Total") @field:PositiveOrZero
    var invoice_tax_total: BigDecimal,

    @Label("Discount Amount") @field:PositiveOrZero
    var discount_amount: BigDecimal

) : Base()