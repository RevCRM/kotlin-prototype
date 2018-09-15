package org.revcrm.models.common

import javax.persistence.*

@Entity
data class SelectionList(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val code: String,
    val label: String
)

@Entity
data class SelectionOption(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(
        name = "listId",
        foreignKey = ForeignKey(name = "fk_list_id")
    )
    val list: SelectionList,

    val code: String,
    val label: String
)
