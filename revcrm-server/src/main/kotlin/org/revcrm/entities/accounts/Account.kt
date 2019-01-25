package org.revcrm.entities.accounts

import org.revcrm.annotations.Label
import org.revcrm.annotations.MultiLine
import org.revcrm.annotations.Validate
import org.revcrm.annotations.SelectionList
import org.revcrm.db.EntityValidationData
import org.revcrm.entities.Base
import xyz.morphia.annotations.Embedded
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Index
import xyz.morphia.annotations.Indexes
import xyz.morphia.annotations.Field as IndexField
import xyz.morphia.utils.IndexType
import javax.validation.Valid

@Entity
@Indexes(
    Index(fields = [
        IndexField(value = "$**", type = IndexType.TEXT)
    ])
)
class Account(
    @Label("Is Company?")
    var is_company: Boolean,

    @Label("Company Name")
    var company_name: String?,

    @Label("Title")
    @SelectionList("contact_titles")
    var title: String?,

    @Label("First Name")
    var first_name: String?,

    @Label("Last Name")
    var last_name: String?,

    @Label("Account Code")
    var code: String?,

    @Label("Tags")
    @SelectionList("account_tags")
    var tags: String?,

    @Label("Source")
    @SelectionList("account_sources")
    var source: String?,

    @Label("Phone")
    var phone: String?,

    @Label("Mobile")
    var mobile: String?,

    @Label("Fax")
    var fax: String?,

    @Label("Email")
    var email: String?,

    @Label("Website")
    var website: String?,

    @Label("Notes")
    @MultiLine
    var notes: String?,

    @Label("Primary Address")
    @Embedded @field:Valid
    var primary_address: Address

) : Base() {

    @Label("Name")
    override val record_name: String
    get() {
        return if (is_company)
            company_name.toString()
        else
            "${title ?: ""} ${first_name ?: ""} ${last_name ?: ""}".trim()
    }

    @Validate
    fun validate(validation: EntityValidationData) {
        if (is_company && company_name.isNullOrBlank()) {
            validation.addFieldError(this,
                fieldPath = "company_name",
                errorCode = "NotBlank",
                message = "Company Name cannot be blank")
        } else if (!is_company && last_name.isNullOrBlank()) {
            validation.addFieldError(this,
                fieldPath = "last_name",
                errorCode = "NotBlank",
                message = "Contact Last Name cannot be blank")
        }
    }
}
