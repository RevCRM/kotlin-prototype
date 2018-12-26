package org.revcrm.testdb

import com.mongodb.BasicDBObject
import xyz.morphia.Datastore

val TEST_ACCOUNTS = listOf(
    Account("Bobs Buildings Ltd", "0210 1234", "bob@bbuildings.co.nz", 5),
    Account("Annes Antiques Inc", "11223344", "anne@oldstuff.com", 3),
    Account("Dereks Dogs Co", "55443322", "derek@woof.org", 8)
)

fun resetAccountData(ds: Datastore) {
    val col = ds.getCollection(Account::class.java)
    col.remove(BasicDBObject()) // remove all items
    TEST_ACCOUNTS.forEach { ds.save(it) }
}