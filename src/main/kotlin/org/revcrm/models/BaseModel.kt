package org.revcrm.models

import java.sql.Timestamp
import java.time.Instant
import javax.persistence.*



@MappedSuperclass
abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0

    lateinit var createdDate: Timestamp
    lateinit var updatedDate: Timestamp

    @PrePersist
    fun prePersist() {
        createdDate = Timestamp.from(Instant.now())
        updatedDate = createdDate
    }

    @PreUpdate
    fun preUpdate() {
        updatedDate = Timestamp.from(Instant.now())
    }

}