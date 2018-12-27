package org.revcrm.testdb

import com.mongodb.BasicDBObject
import xyz.morphia.Datastore

val TEST_ACCOUNTS = listOf(
    Account("Bobs Buildings Ltd", "Bob", "0210 1234", "bob@bbuildings.co.nz", 5),
    Account("Annes Antiques Inc", "Anne", "11223344", "anne@oldstuff.com", 3),
    Account("Dereks Dogs Co", "Joan", "55443322", "derek@woof.org", 8),
    Account("Dereks Dogs Co", "Derek", "55443322", "joan@woof.org", 8)
)

fun resetAccountData(ds: Datastore) {
    val col = ds.getCollection(Account::class.java)
    col.remove(BasicDBObject()) // remove all items
    TEST_ACCOUNTS.forEach { ds.save(it) }
}

fun resetTestModel2Data(ds: Datastore) {
    val col = ds.getCollection(TestModel2::class.java)
    col.remove(BasicDBObject()) // remove all items
    for (i in 1..30) {
        ds.save(TestModel2("Record $i", i))
    }
}