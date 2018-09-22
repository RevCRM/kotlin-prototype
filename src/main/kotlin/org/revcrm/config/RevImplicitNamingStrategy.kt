package org.revcrm.config

import org.hibernate.boot.model.naming.Identifier
import org.hibernate.boot.model.naming.ImplicitForeignKeyNameSource
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl

class RevImplicitNamingStrategy : ImplicitNamingStrategyJpaCompliantImpl() {

    override fun determineForeignKeyName(source: ImplicitForeignKeyNameSource): Identifier {
        return toIdentifier("FK_" + source.tableName.canonicalName + "_" + source.referencedTableName.canonicalName, source.buildingContext)
    }

}