package org.revcrm.annotations

/**
 * Used purely so that kotlin.plugin.noarg can create an empty constructor for morphia
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EmbeddedEntity