package org.revcrm.data

import org.hibernate.Session

interface IRevCRMData {
    fun initialise()
    fun <T>withTransaction(method: (Session) -> T): T
}