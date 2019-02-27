package org.revcrm.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("revcrm.db")
class DBConfig {
    lateinit var url: String
    lateinit var name: String
    lateinit var entityClasses: List<String>
    lateinit var embeddedClasses: List<String>
}
