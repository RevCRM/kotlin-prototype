package org.revcrm.testdb

import com.mongodb.BasicDBObject
import xyz.morphia.Datastore

val TEST_ACCOUNTS = listOf(
    Account("Bobs Buildings Ltd", "Bob", "0210 1234", "bob@bbuildings.co.nz", 5),
    Account("Annes Antiques Inc", "Anne", "11223344", "anne@oldstuff.com", 3),
    Account("Dereks Dogs Co", "Joan", "55443322", "derek@woof.org", 8),
    Account("Dereks Dogs Co", "Derek", "55443322", "joan@woof.org", 8)
)

val TEST_EMBEDDED_ENTITIES = listOf(
    TestWithEmbeddedEntity("Test 1", listOf(
        TestEmbeddedEntity("Option 1"), TestEmbeddedEntity("Option 2"))),
    TestWithEmbeddedEntity("Test 2", listOf(
        TestEmbeddedEntity("Option 3"))),
    TestWithEmbeddedEntity("Test 3", null)
)

val TEST_STRING_LISTS = listOf(
    TestWithStringList("List 1", listOf("Option 1", "Option 2", "Option 3")),
    TestWithStringList("List 2", listOf("Option 4", "Option 5"))
)

fun deleteAccountData(ds: Datastore) {
    val col = ds.getCollection(Account::class.java)
    col.remove(BasicDBObject()) // remove all items
}

fun resetAccountData(ds: Datastore) {
    deleteAccountData(ds)
    TEST_ACCOUNTS.forEach { ds.save(it) }
}

fun resetTestEntity2Data(ds: Datastore) {
    val col = ds.getCollection(TestEntity2::class.java)
    col.remove(BasicDBObject()) // remove all items
    for (i in 1..30) {
        ds.save(TestEntity2("Record $i", i))
    }
}

fun resetEmbeddedEntityData(ds: Datastore) {
    val col = ds.getCollection(TestWithEmbeddedEntity::class.java)
    col.remove(BasicDBObject()) // remove all items
    TEST_EMBEDDED_ENTITIES.forEach { ds.save(it) }
}

fun deleteStringListData(ds: Datastore) {
    val col = ds.getCollection(TestWithStringList::class.java)
    col.remove(BasicDBObject()) // remove all items
}

fun resetStringListData(ds: Datastore) {
    deleteStringListData(ds)
    TEST_STRING_LISTS.forEach { ds.save(it) }
}