package org.revcrm.models.common

import org.revcrm.models.BaseModel
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class SelectionOption(

    @ManyToOne
    @JoinColumn(name = "listId")
    val list: SelectionList,
    val code: String,
    val label: String,
    val seq: Short

): BaseModel()
