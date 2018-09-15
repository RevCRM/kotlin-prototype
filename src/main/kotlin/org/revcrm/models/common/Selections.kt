package org.revcrm.models.common

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.revcrm.data.YMLEntityResolver
import org.revcrm.models.BaseModel
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "dataId",
    resolver = YMLEntityResolver::class)
data class SelectionList(
    val model: String,
    val label: String
): BaseModel()

@Entity
data class SelectionOption(

    @ManyToOne
    @JoinColumn(
        name = "listId",
        foreignKey = ForeignKey(name = "fk_list_id")
    )
    val list: SelectionList,
    val code: String,
    val label: String,
    val seq: Short

): BaseModel()
