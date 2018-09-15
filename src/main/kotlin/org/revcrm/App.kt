package org.revcrm

// To Investigate
// DI for Database Stuff
// http://bisaga.com/blog/programming/sakila-dagger-2-dependency-injection/

fun main(args: Array<String>) {
    println("Starting RevCRM...")

    val revcrm = DaggerRevCRMComponent
            .builder()
            .build()

    val loader = revcrm.getLoader()
    loader.importData()

    // Make sure we clean up after ourselves :)
    revcrm.getDatabase().close()

//    if ( sessionFactory != null ) {
//        // Do Database Stuff!...
//
//        val session = sessionFactory.openSession()
//        session.beginTransaction()
//        session.save(Account(
//                name = "Briggs Bikes Ltd"
//        ))
//        session.save(Account(
//                name = "Owen Mowers Ltd"
//        ))
//        session.getTransaction().commit()
//        session.close()
//
//        println("importing data...")
//        importData()
//
//        sessionFactory.close()
//
//        println("Done!")
//
//    }
}

