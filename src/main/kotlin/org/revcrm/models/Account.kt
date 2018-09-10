package org.revcrm.models

import javax.persistence.*

@Entity
data class Account(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Int,

    @Column(nullable = false)
    val name: String
)