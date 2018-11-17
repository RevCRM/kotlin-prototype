package org.revcrm.models

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Int = 0

    @CreationTimestamp
    var created_date: LocalDateTime? = null

    @UpdateTimestamp
    var updated_date: LocalDateTime? = null

}