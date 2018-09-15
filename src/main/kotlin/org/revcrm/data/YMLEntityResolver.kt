package org.revcrm.data

import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey
import com.fasterxml.jackson.annotation.ObjectIdResolver
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver
import java.util.Objects.requireNonNull
import javax.inject.Inject

// https://stackoverflow.com/questions/41989906/jackson-referencing-an-object-as-a-property

class YMLEntityResolver @Inject constructor(_db: Database): SimpleObjectIdResolver() {
    val db: Database = _db

    override fun bindItem(id: IdKey, pojo: Any) {
        super.bindItem(id, pojo)
    }

    override fun resolveId(id: IdKey): Any? {
        var resolved: Any? = super.resolveId(id)
        if (resolved == null) {
            resolved = _tryToLoadFromSource(id)
            bindItem(id, resolved)
        }

        return resolved
    }

    private fun _tryToLoadFromSource(idKey: IdKey): Any {
        requireNonNull(idKey.scope, "global scope is not supported")

        val id = idKey.key as String
        val poType = idKey.scope

        return db.withTransaction { it.get(poType, id) }
    }

    override fun newForDeserialization(context: Any?): ObjectIdResolver {
        return YMLEntityResolver(db)
    }

    override fun canUseFor(resolverType: ObjectIdResolver): Boolean {
        return resolverType.javaClass == YMLEntityResolver::class.java
    }

}