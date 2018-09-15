package org.revcrm.config

import org.apache.commons.lang3.StringUtils
import org.hibernate.boot.model.naming.Identifier
import org.hibernate.boot.model.naming.PhysicalNamingStrategy
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment
import java.util.*

class PhysicalNamingStrategy : PhysicalNamingStrategy {

    override fun toPhysicalCatalogName(name: Identifier?, jdbcEnvironment: JdbcEnvironment): Identifier? {
        return name
    }

    override fun toPhysicalSchemaName(name: Identifier?, jdbcEnvironment: JdbcEnvironment): Identifier? {
        return name
    }

    override fun toPhysicalTableName(name: Identifier?, jdbcEnvironment: JdbcEnvironment): Identifier? {
        val parts = splitAndReplace(name!!.getText())
        return jdbcEnvironment.identifierHelper.toIdentifier(
                join(parts),
                name.isQuoted()
        )
    }

    override fun toPhysicalSequenceName(name: Identifier?, jdbcEnvironment: JdbcEnvironment): Identifier? {
        return name
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun toPhysicalColumnName(name: Identifier?, jdbcEnvironment: JdbcEnvironment): Identifier? {
        val parts = splitAndReplace(name!!.getText())
        return jdbcEnvironment.identifierHelper.toIdentifier(
                join(parts),
                name!!.isQuoted()
        )
    }

    private fun splitAndReplace(name: String): LinkedList<*> {
        val result = LinkedList<Any>()
        for (part in StringUtils.splitByCharacterTypeCamelCase(name)) {
            if (part == null || part.trim({ it <= ' ' }).isEmpty()) {
                continue
            }
            result.add(part.toUpperCase(Locale.ROOT))
        }
        return result
    }

    private fun join(parts: List<*>): String {
        var firstPass = true
        var separator = ""
        val joined = StringBuilder()
        for (part in parts) {
            joined.append(separator).append(part)
            if (firstPass) {
                firstPass = false
                separator = "_"
            }
        }
        return joined.toString()
    }
}