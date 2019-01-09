package org.revcrm.config

data class Config(
    val dbUrl: String,
    val dbName: String,
    val entityClasses: List<String>,
    val embeddedClasses: List<String>
)