package org.revcrm.config

data class Config (
    val dbUrl: String,
    val entityPackages: List<String>
)