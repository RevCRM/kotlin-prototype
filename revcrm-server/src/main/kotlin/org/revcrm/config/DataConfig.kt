package org.revcrm.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("revcrm")
class DataConfig {
    lateinit var data: List<String>
}
