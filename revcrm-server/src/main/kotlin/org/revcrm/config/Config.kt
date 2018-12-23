package org.revcrm.config

data class Config (
    val dbUrl: String,
    val dbName: String,
    val entityPackages: List<String>
)