package org.revcrm.entities.invoices

import org.revcrm.annotations.EmbeddedEntity
import org.revcrm.annotations.Label
import org.revcrm.entities.Base
import xyz.morphia.annotations.Embedded
import xyz.morphia.annotations.Entity
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

@EmbeddedEntity
class InvoiceLine(

    @Label("Item") @field:NotBlank
    var item: String,

    @Label("Quantity") @field:Positive
    var quantity: BigDecimal,

    @Label("Unit Price") @field:PositiveOrZero
    var unit_prive: BigDecimal,

    @Label("Line Tax") @field:PositiveOrZero
    var line_tax: BigDecimal,

    @Label("Discount (%)") @field:PositiveOrZero
    var discount_percentage: BigDecimal,

    @Label("Discount Amount") @field:PositiveOrZero
    var discount_amount: BigDecimal

)

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
    @Label("Invoice Lines") @field:Size(min = 1)
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