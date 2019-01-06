package org.revcrm.entities.accounts

import org.revcrm.annotations.Label
import org.revcrm.annotations.MultiLine
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
    var title: String?,

    @Label("First Name")
    var first_name: String?,

    @Label("Last Name")
    var last_name: String?,

    @Label("Account Code")
    var code: String?,

    @Label("Tags")
    var tags: String?,

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

) : Base()
