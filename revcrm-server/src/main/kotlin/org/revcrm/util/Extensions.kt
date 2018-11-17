package org.revcrm.util

import org.hibernate.Session
import javax.persistence.EntityManager

val EntityManager.session: Session
    get() = unwrap(Session::class.java)