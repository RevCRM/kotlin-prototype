package org.revcrm.data

import org.hibernate.Session

interface IRevCRMData {
    fun <T>withTransaction(method: (Session) -> T): T
    fun close()
}