package org.revcrm.db

import xyz.morphia.mapping.MappedField
import com.mongodb.DBObject
import xyz.morphia.AbstractEntityInterceptor
import xyz.morphia.mapping.Mapper

class EntityValidator : AbstractEntityInterceptor() {

    override fun prePersist(ent: Any, dbObj: DBObject, mapper: Mapper) {
        val mc = mapper.getMappedClass(ent)
        val fieldsToTest = mc.persistenceFields // mc.getFieldsAnnotatedWith(NonNull::class.java)
        for (mf in fieldsToTest) {
            if (mf.getFieldValue(ent) == null) {
                throw ValidationException(mf)
            }
        }
    }

    internal class ValidationException(mf: MappedField) :
        RuntimeException("field is not valid " + mf.fullName)
}