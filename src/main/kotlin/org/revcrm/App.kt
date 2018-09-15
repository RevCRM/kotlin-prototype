package org.revcrm

// To Investigate
// DI for Database Stuff
// http://bisaga.com/blog/programming/sakila-dagger-2-dependency-injection/

fun main(args: Array<String>) {
    println("Starting RevCRM...")

    val loader = DaggerRevCRMComponent
            .builder()
            .build()
            .getLoader()

    loader.importData()

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

