package org.revcrm

import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.revcrm.models.accounts.Account

// To Investigate
// DI for Database Stuff
// http://bisaga.com/blog/programming/sakila-dagger-2-dependency-injection/

fun main(args: Array<String>) {
    println("Starting RevCRM...")

    val registry = StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build()

    var sessionFactory: SessionFactory? = null
    try {
        sessionFactory = MetadataSources(registry).buildMetadata().buildSessionFactory()
    } catch (e: Exception) {
        // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
        // so destroy it manually.
        println("Exception creating factory.")
        e.printStackTrace()
        StandardServiceRegistryBuilder.destroy(registry)
    }

    if ( sessionFactory != null ) {
        // Do Database Stuff!...

        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.save(Account(
                name = "Briggs Bikes Ltd"
        ))
        session.save(Account(
                name = "Owen Mowers Ltd"
        ))
        session.getTransaction().commit()
        session.close()

        sessionFactory.close()

        println("Done!")

    }
}

