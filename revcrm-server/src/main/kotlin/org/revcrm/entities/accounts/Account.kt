package org.revcrm.entities.accounts

import org.revcrm.annotations.Label
import org.revcrm.annotations.MultiLine
import org.revcrm.annotations.Validate
import org.revcrm.annotations.SelectionList
import org.revcrm.db.EntityValidationData
import org.revcrm.entities.Base
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Index
import xyz.morphia.annotations.Indexes
import xyz.morphia.annotations.Field as IndexField
import xyz.morphia.utils.IndexType

@Entity
@Indexes(
    Index(fields = [
        IndexField(value = "$**", type = IndexType.TEXT)
    ])
)
class Account(
    @Label("Is Organisation?")
    var is_org: Boolean,

    @Label("Organisation Name")
    var org_name: String?,

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
    var notes: String?

//    @ManyToOne
//    @JoinColumn(name = "primary_address_id")
//    @Label("Address")
//    var primary_address: Address

) : Base() {

    @Validate
    fun validate(validation: EntityValidationData) {
        if (is_org && org_name.isNullOrBlank()) {
            validation.addFieldError(this,
                fieldPath = "org_name",
                errorCode = "NotBlank",
                message = "Organisation Name cannot be blank")
        } else if (!is_org && last_name.isNullOrBlank()) {
            validation.addFieldError(this,
                fieldPath = "last_name",
                errorCode = "NotBlank",
                message = "Last Name cannot be blank")
        }
    }
}
