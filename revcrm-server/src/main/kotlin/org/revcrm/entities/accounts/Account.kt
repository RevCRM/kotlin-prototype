package org.revcrm.entities.accounts

import org.revcrm.annotations.Field
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
    @Field("Is Organisation?")
    var is_org: Boolean,

    @Field("Organisation Name")
    var org_name: String?,

    @Field("Title")
    var title: String?,

    @Field("First Name")
    var first_name: String?,

    @Field("Last Name")
    var last_name: String?,

    @Field("Account Code")
    var code: String?,

    @Field("Tags")
    var tags: String?,

    @Field("Phone")
    var phone: String?,

    @Field("Mobile")
    var mobile: String?,

    @Field("Fax")
    var fax: String?,

    @Field("Email")
    var email: String?,

    @Field("Website")
    var website: String?,

    @Field("Notes")
    var notes: String?

//    @ManyToOne
//    @JoinColumn(name = "primary_address_id")
//    @Field("Address")
//    var primary_address: Address

) : Base()
