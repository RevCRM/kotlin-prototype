package org.revcrm.annotations

/**
 * Used to indicate that a readonly property should be stored in the DB
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Stored