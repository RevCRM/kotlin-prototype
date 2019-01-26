package org.revcrm.db

import xyz.morphia.mapping.DefaultCreator

class EntityCreator(
    private val context: EntityContext
) : DefaultCreator() {

    override fun <T> createInstance(clazz: Class<T>): T {
        val instance = super.createInstance(clazz)
        if (instance is WithEntityContext) {
            instance.context = context
        }
        return instance
    }
}