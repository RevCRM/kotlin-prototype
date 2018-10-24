package org.revcrm.models

import java.sql.Timestamp
import java.time.Instant
import javax.persistence.*

@MappedSuperclass
abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0

    var created_date: Timestamp? = null
    var updated_date: Timestamp? = null

    @PrePersist
    fun prePersist() {
        created_date = Timestamp.from(Instant.now())
        updated_date = created_date
    }

    @PreUpdate
    fun preUpdate() {
        updated_date = Timestamp.from(Instant.now())
    }

}